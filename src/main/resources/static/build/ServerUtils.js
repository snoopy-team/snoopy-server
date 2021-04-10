// Wraps a raw server update to have it conform to an actual ServerUpdate. This means taking the
// data in a raw server update and converting it into Vec2's, instead of the arrays of size 2 which
// are given from the server.
var RawServerUpdateWrapper = /** @class */ (function () {
    function RawServerUpdateWrapper(rawServerUpdate) {
        console.log('raw:', rawServerUpdate);
        // Translate all arrays of size 2, which represent vectors, to `Vec2`s
        // Convert players data
        for (var _i = 0, _a = rawServerUpdate['players']; _i < _a.length; _i++) {
            var player = _a[_i];
            var acceleration = player['acceleration'];
            player['acceleration'] = { x: acceleration[0], y: acceleration[1] };
            var position = player['position'];
            player['position'] = { x: position[0], y: position[1] };
            var velocity = player['velocity'];
            player['velocity'] = { x: velocity[0], y: velocity[1] };
        }
        // Convert bullets data
        for (var _b = 0, _c = rawServerUpdate['bullets'][0]; _b < _c.length; _b++) { // FIXME
            var bullet = _c[_b];
            console.log('should not be reaching this line');
            var position = bullet['position'];
            bullet['position'] = { x: position[0], y: position[1] };
            var velocity = bullet['velocity'];
            bullet['velocity'] = { x: velocity[0], y: velocity[1] };
        }
        // Set properties of this ServerUpdate
        this.players = rawServerUpdate['players'];
        this.bullets = []; // rawServerUpdate['bullets'];
    }
    return RawServerUpdateWrapper;
}());
/**
 * Holds a server and provides information about the latest update, if there has been an update
 * since the last time a client checked.
 */
var ServerUpdateManager = /** @class */ (function () {
    /**
     * Constructs a new ServerUpdateManager that has no initial updates.
     * @param serverUpdateProvider whatever the source of information is, whether an actual server or
     * a local server with mock data (i.e. ServerMock).
     */
    function ServerUpdateManager(serverUpdateProvider) {
        var _this = this;
        /**
         * Returns if there's been a new update since the last time the client got an update
         */
        this.hasUpdate = function () { return _this.hasUpdateFlag; };
        /**
         * Gets the most recent update. Calling this will make hasUpdate() return false.
         */
        this.getUpdate = function () {
            _this.hasUpdateFlag = false;
            return _this.mostRecentUpdate;
        };
        /**
         * Register a new update with this update manager
         */
        this.acceptUpdate = function (update) {
            _this.hasUpdateFlag = true;
            _this.mostRecentUpdate = update;
        };
        /**
         * Tells the server that we're ready to get updates at which point, it will provide us with a
         * stream of updates.
         */
        this.beginRequestingUpdates = function () {
            _this.serverUpdateProvider.addUpdateListener(_this.acceptUpdate);
            _this.serverUpdateProvider.startProvidingUpdates();
        };
        this.serverUpdateProvider = serverUpdateProvider;
        this.hasUpdateFlag = false;
        this.mostRecentUpdate = {
            players: [],
            bullets: []
        };
    }
    return ServerUpdateManager;
}());
export { ServerUpdateManager };
/**
 * A class that will help us visually test our code by mocking input from the server.
 */
var ServerMock = /** @class */ (function () {
    function ServerMock() {
        var _this = this;
        /**
         * Starts providing mock data
         */
        this.startProvidingUpdates = function () {
            _this.oneSecondSineMotion();
        };
        /**
         * Add the given update callback to call when we receive a new update from the server
         */
        this.addUpdateListener = function (onUpdate) {
            _this.updateObservers.push(onUpdate);
        };
        /**
         * Provide the given update to all observers to this server
         */
        this.broadcastUpdate = function (update) {
            _this.updateObservers.forEach(function (updateObserver) { return updateObserver(update); });
        };
        /**
         * Provides some manually defined updates to the server manager at a rate of one second.
         */
        this.oneSecIntervalUpdates = function () {
            var mockData = [
                {
                    players: [{
                            id: 'example player id',
                            position: { x: 0, y: 0 },
                            velocity: { x: 200, y: 200 },
                            acceleration: { x: 0, y: -50 },
                            orientation: 0,
                            cooldown: 0,
                        }],
                    bullets: []
                },
                {
                    players: [{
                            id: 'example player id',
                            position: { x: 100, y: 100 },
                            velocity: { x: 5, y: 5 },
                            acceleration: { x: 0, y: 0 },
                            orientation: Math.PI,
                            cooldown: 0,
                        }],
                    bullets: []
                },
                {
                    players: [{
                            id: 'example player id',
                            position: { x: 100, y: 50 },
                            velocity: { x: 0, y: 0 },
                            acceleration: { x: 0, y: 0 },
                            orientation: 2 * Math.PI,
                            cooldown: 0,
                        }],
                    bullets: [{
                            id: 'example bullet id',
                            position: { x: 50, y: 50 },
                            velocity: { x: 30, y: 5 }
                        }]
                },
            ];
            var i = 0;
            setInterval(function () {
                if (i < mockData.length) {
                    _this.broadcastUpdate(mockData[i]);
                    i++;
                }
            }, 1000);
        };
        this.oneSecondSineMotion = function () {
            var flag = true;
            var sineMotion = function (toggleFlag) {
                if (toggleFlag) {
                    return {
                        players: [{
                                id: 'example player id',
                                position: { x: 0, y: 0 },
                                velocity: { x: 300, y: 300 },
                                acceleration: { x: 0, y: 0 },
                                orientation: 0,
                                cooldown: 0,
                            }],
                        bullets: []
                    };
                }
                else {
                    return {
                        players: [{
                                id: 'example player id',
                                position: { x: 300, y: 300 },
                                velocity: { x: -300, y: -300 },
                                acceleration: { x: 0, y: 0 },
                                orientation: 0,
                                cooldown: 0,
                            }],
                        bullets: []
                    };
                }
            };
            setInterval(function () {
                _this.broadcastUpdate(sineMotion(flag));
                flag = !flag;
            }, 1000);
        };
        this.updateObservers = [];
    }
    return ServerMock;
}());
export { ServerMock };
/**
 * A message receiver for the live server.
 */
var LiveServer = /** @class */ (function () {
    function LiveServer() {
        var _this = this;
        this.addUpdateListener = function (listener) {
            _this.updateObservers.push(listener);
        };
        this.startProvidingUpdates = function () {
            // // Attempt 1: Raw websockets
            // let webSocket = new WebSocket(constants.SERVER_SOCKET_URL);
            // webSocket.onclose = () => console.log('socket closed');
            // webSocket.onerror = (e) => console.log('Error:', e);
            // webSocket.onopen = () => {
            //   console.log('Server connected successfully');
            //   // Keep track of which keys are down at any point in time and emit a message to server when
            //   // keys are pressed
            //   document.addEventListener('keydown', (e) => {
            //     this.keysDown.push(e.key.toLowerCase());
            //     webSocket.send(this.keysDown.join(','));
            //   });
            //   document.addEventListener('keyup', (e) => {
            //     this.keysDown = this.keysDown.filter((key) => key != e.key.toLowerCase());
            //     webSocket.send(this.keysDown.join(','));
            //   });
            //   webSocket.onmessage = (e) => {
            //     let message: string = e.data;
            //     if (message == 'some message title') {
            //       console.log(message);
            //     }
            //   }
            // }
            // // Attempt 2: Socket.io
            // // Connect to remote socket for AI and multiplayer functionality
            // const socket = (window as any).io(constants.SERVER_SOCKET_URL) as Socket;
            // socket.on('connect', () => {
            //   let decoder = new TextDecoder("utf-8");
            //   console.log('Server connected successfully');
            //   // Keep track of which keys are down at any point in time and emit a message to server when
            //   // keys are pressed
            //   document.addEventListener('keydown', (e) => {
            //     this.keysDown.push(e.key.toLowerCase());
            //     socket.emit('example message', this.keysDown.join(','));
            //   });
            //   document.addEventListener('keyup', (e) => {
            //     this.keysDown = this.keysDown.filter((key) => key != e.key.toLowerCase());
            //     socket.emit('example message', this.keysDown.join(','));
            //   });
            //   socket.on('example message', (data) => {
            //     // this is where I take `data` and broadcast an update
            //     console.log(this.getFirstJSONUpdate(decoder.decode(data)));
            //     // this.broadcastUpdate(JSON.parse(decoder.decode(data)));
            //   });
            //   socket.on('disconnect', () => {
            //     console.log('Server disconnected');
            //   });
            // });
            // // Attempt 3: Using a Maintained STOMP (protocol for websockets) library
            // let stompClient: (StompJs.Client as Client);
            // const stompConfig = {
            //   // Typically login, passcode and vhost
            //   // Adjust these for your broker
            //   // connectHeaders: {
            //   //   login: "guest",
            //   //   passcode: "guest"
            //   // },
            //   // Broker URL, should start with ws:// or wss:// - adjust for your broker setup
            //   // brokerURL: "ws://localhost:15674/ws",
            //   brokerURL: constants.SERVER_SOCKET_URL,
            //   // Keep it off for production, it can be quit verbose
            //   // Skip this key to disable
            //   debug: function (str: string) {
            //     console.log('STOMP: ' + str);
            //   },
            //   // If disconnected, it will retry after 200ms
            //   reconnectDelay: 200,
            //   // Subscriptions should be done inside onConnect as those need to reinstated when the broker reconnects
            //   onConnect: (frame: any) => {
            //     // The return object has a method called `unsubscribe`
            //     const subscription = stompClient.subscribe('/topic/chat', (message: IMessage) => {
            //       const payload = JSON.parse(message.body);
            //       // Do something with `payload` (object with info from server)
            //       console.log(payload)
            //     });
            //   }
            // };
            // // Create an instance
            // stompClient = new StompJs.Client(stompConfig);
            // // You can set additional configuration here
            // // Attempt to connect
            // stompClient.activate();
            // // Attempt 4: Use a deprecated STOMP library :(
            // Ignore the fact that we're using SockJS and Stomp for websockets.
            // @ts-ignore 
            var socket = new SockJS('/gs-guide-websocket');
            // @ts-ignore
            var stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/greetings', function (greeting) {
                    // console.log(JSON.parse(greeting.body));
                    _this.broadcastUpdate(new RawServerUpdateWrapper(JSON.parse(greeting.body)));
                });
                // Keep track of which keys are down at any point in time
                document.addEventListener('keydown', function (e) {
                    if (!_this.keysDown.includes(e.key)) {
                        _this.keysDown.push(e.key.toLowerCase());
                    }
                });
                document.addEventListener('keyup', function (e) {
                    _this.keysDown = _this.keysDown.filter(function (key) { return key != e.key.toLowerCase(); });
                });
                // Every 30 ms, ping the server with our current keysdown because we made the design
                // decision that the frontend should control every time the backend game loop steps forward 1
                // time step. :(
                setInterval(function () {
                    var out = JSON.stringify({ actions: _this.keysDown });
                    stompClient.send("/app/hello", {}, out);
                }, 30);
            });
        };
        /**
         * Provide the given update to all observers to this server
         */
        this.broadcastUpdate = function (update) {
            _this.updateObservers.forEach(function (updateObserver) { return updateObserver(update); });
        };
        this.updateObservers = [];
        this.keysDown = [];
    }
    // TODO
    LiveServer.prototype.getFirstJSONUpdate = function (jsonStr) {
        var i = 1;
        var numClosingBracesNeeded = 1;
        var firstJSONObj = "";
        var currChar = jsonStr[0];
        while (currChar != undefined) {
            var currChar_1 = jsonStr[i];
            if (jsonStr[i] == '{') {
                numClosingBracesNeeded++;
            }
            else if (jsonStr[i] == '}') {
                numClosingBracesNeeded--;
            }
            firstJSONObj += currChar_1;
            if (numClosingBracesNeeded == 0) {
                return firstJSONObj;
            }
            i++;
        }
        return "";
    };
    return LiveServer;
}());
export { LiveServer };
//# sourceMappingURL=ServerUtils.js.map