// Wraps a raw server update to have it conform to an actual ServerUpdate. This means taking the
// data in a raw server update and converting it into Vec2's, instead of the arrays of size 2 which
// are given from the server.
var RawServerUpdateWrapper = /** @class */ (function () {
    function RawServerUpdateWrapper(rawServerUpdate) {
        // console.log('raw:',rawServerUpdate);
        // Translate all arrays of size 2, which represent vectors, to `Vec2`s
        // Convert players data
        for (var playerID in rawServerUpdate['players']) {
            var player = rawServerUpdate['players'][playerID];
            var acceleration = player['acceleration'];
            player['acceleration'] = { x: acceleration[0], y: acceleration[1] };
            var position = player['position'];
            player['position'] = { x: position[0], y: position[1] };
            var velocity = player['velocity'];
            player['velocity'] = { x: velocity[0], y: velocity[1] };
        }
        // Convert bullets data
        for (var bulletID in rawServerUpdate['bullets']) {
            for (var _i = 0, _a = rawServerUpdate['bullets'][bulletID]; _i < _a.length; _i++) {
                var bullet = _a[_i];
                var position = bullet['position'];
                bullet['position'] = { x: position[0], y: position[1] };
                var velocity = bullet['velocity'];
                bullet['velocity'] = { x: velocity[0], y: velocity[1] };
            }
        }
        // Set properties of this ServerUpdate
        this.players = rawServerUpdate['players'];
        this.bullets = rawServerUpdate['bullets'];
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
        /**
         * Sends a message to the endpoint that we're interacting with.
         */
        this.sendMessage = function (msg) {
            _this.serverUpdateProvider.sendMessage(msg);
        };
        this.serverUpdateProvider = serverUpdateProvider;
        this.hasUpdateFlag = false;
        this.mostRecentUpdate = {
            players: {},
            bullets: {}
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
                    players: {
                        0: {
                            position: { x: 0, y: 0 },
                            velocity: { x: 200, y: 200 },
                            acceleration: { x: 0, y: -50 },
                            orientation: 0,
                            cooldown: 0,
                        },
                    },
                    bullets: {}
                },
                {
                    players: {
                        0: {
                            position: { x: 100, y: 100 },
                            velocity: { x: 5, y: 5 },
                            acceleration: { x: 0, y: 0 },
                            orientation: Math.PI,
                            cooldown: 0,
                        }
                    },
                    bullets: {}
                },
                {
                    players: {
                        0: {
                            position: { x: 100, y: 50 },
                            velocity: { x: 0, y: 0 },
                            acceleration: { x: 0, y: 0 },
                            orientation: 2 * Math.PI,
                            cooldown: 0,
                        }
                    },
                    bullets: {}
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
                // if (toggleFlag) {
                return {
                    players: {
                        0: {
                            position: { x: 0, y: 0 },
                            velocity: { x: 300, y: 300 },
                            acceleration: { x: 0, y: 0 },
                            orientation: 0,
                            cooldown: 0,
                        },
                        1: {
                            position: { x: 0, y: 300 },
                            velocity: { x: 300, y: -300 },
                            acceleration: { x: 0, y: 0 },
                            orientation: 0,
                            cooldown: 0,
                        }
                    },
                    bullets: {
                        0: [
                            {
                                position: { x: 0, y: 50 },
                                velocity: { x: 50, y: 0 },
                            },
                            // {
                            //   position: { x: 50, y: 0 },
                            //   velocity: { x: 0, y: 50 },
                            // },
                        ],
                        1: [
                            {
                                position: { x: 0, y: 0 },
                                velocity: { x: 50, y: 100 },
                            },
                            // {
                            //   position: { x: 50, y: 50 },
                            //   velocity: { x: 0, y: -50 },
                            // },
                        ],
                    }
                };
                // } else {
                //   return {
                //     players: [
                //       {
                //         id: 'example player id',
                //         position: { x: 300, y: 300 },
                //         velocity: { x: -300, y: -300 },
                //         acceleration: { x: 0, y: 0 },
                //         orientation: 0,
                //         cooldown: 0,
                //       },
                //       {
                //         id: 'example player id 2',
                //         position: { x: 300, y: 0 },
                //         velocity: { x: -300, y: 300 },
                //         acceleration: { x: 0, y: 0 },
                //         orientation: 0,
                //         cooldown: 0,
                //       }
                //     ],
                //     bullets: []
                //   }
                // }
            };
            setInterval(function () {
                _this.broadcastUpdate(sineMotion(flag));
                flag = !flag;
            }, 1000);
        };
        this.updateObservers = [];
    }
    /**
     * Does nothing because a ServerMock doesn't actually conncet to a Server.
     */
    ServerMock.prototype.sendMessage = function (msg) {
        // do nothing
    };
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
            // this.stompClient.debug = null
            _this.stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                _this.stompClient.subscribe('/game/to-client', function (greeting) {
                    // console.log(JSON.parse(greeting.body));
                    _this.broadcastUpdate(new RawServerUpdateWrapper(JSON.parse(greeting.body)));
                });
                // Every 30 ms, ping the server with our current keysdown because we made the design
                // decision that the frontend should control every time the backend game loop steps forward 1
                // time step.
                setInterval(function () {
                    var out = _this.currentMessage;
                    _this.stompClient.send("/app/to-server", {}, out);
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
        // Ignore the fact that we're using client libraries (as opposed to NPM) for SockJS and Stomp to
        // websockets.
        // @ts-ignore 
        var socket = new SockJS('/gs-guide-websocket');
        // @ts-ignore
        this.stompClient = Stomp.over(socket);
        this.currentMessage = JSON.stringify({ actions: [] });
    }
    LiveServer.prototype.sendMessage = function (msg) {
        this.currentMessage = msg;
    };
    return LiveServer;
}());
export { LiveServer };
//# sourceMappingURL=ServerUtils.js.map