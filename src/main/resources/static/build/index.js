import { GameWorld } from './GameWorld.js';
import { IOManager } from './IOManager.js';
import { LiveServer, ServerUpdateManager } from './ServerUtils.js';
export var canvas = document.getElementById('game');
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
// This is what we'll be drawing to the canvas with
export var ctx = canvas.getContext('2d');
// flip y-axis
ctx.transform(1, 0, 0, -1, 0, canvas.height);
export var constants = {
    FPS: 30,
    // Most likely this will change to something a bit more fun (like a blue sky with clouds)
    BACKGROUND_COLOR: 'white',
    // Whether or not debugger info should be displayed
    DEBUG_MODE: true,
    SERVER_SOCKET_URL: 'ws://127.0.0.1:8080/gs-guide-websocket',
    AI_IDX: 0,
    PLAYER_IDX: 1,
    SNOOPY_SIZE: { x: 30, y: 50 },
    BARRON_SIZE: { x: 30, y: 20 },
    TOP_LEFT_WORLD_BOUND: { x: -30, y: 0 },
    BOTTOM_RIGHT_WORLD_BOUND: { x: 1000, y: 1000 },
};
// Initialize an IO manager to keep track of which inputs correspond with which outputs
export var ioManager = new IOManager();
// -------- Load assets --------
// -> Snoopy image
var snoopyImageLoaded = false;
export var imageSnoopy = new Image();
imageSnoopy.onload = function () { return snoopyImageLoaded = true; };
imageSnoopy.src = '../assets/hq/snoopy_hq.png';
// -> Red Barron image
var barronImageLoaded = false;
export var imageBarron = new Image();
imageBarron.onload = function () { return barronImageLoaded = true; };
imageBarron.src = '../assets/hq/red_barron.png';
// -> Clouds 1-4
export var clouds = [new Image(), new Image(), new Image(), new Image()];
var cloudImagesLoaded = [false, false, false, false];
var _loop_1 = function (i) {
    var imageCloud = clouds[i - 1];
    imageCloud.onload = function () { return cloudImagesLoaded[i - 1] = true; };
    imageCloud.src = '../assets/pixelated/clouds/cloud' + i + '.png';
};
for (var i = 1; i <= 4; i++) {
    _loop_1(i);
}
// Start game after assets have loaded. Check if they've loaded every `checkLoadedFreq` milliseconds
var checkLoadedFreq = 33;
var loadTimer = setInterval(function () {
    var allCloudsLoaded = true;
    for (var _i = 0, cloudImagesLoaded_1 = cloudImagesLoaded; _i < cloudImagesLoaded_1.length; _i++) {
        var cloudLoaded = cloudImagesLoaded_1[_i];
        if (!cloudLoaded) {
            allCloudsLoaded = false;
        }
    }
    if (snoopyImageLoaded && barronImageLoaded && allCloudsLoaded) {
        // Stop timer
        clearInterval(loadTimer);
        // Create update manager to serve as the in-between of the server and our game
        var serverUpdateManager = new ServerUpdateManager(new LiveServer());
        // Create world with update manager and begin game
        var world = new GameWorld(serverUpdateManager);
        world.gameLoop();
    }
}, checkLoadedFreq);
//# sourceMappingURL=index.js.map