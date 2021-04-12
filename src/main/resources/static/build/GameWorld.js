import { canvas, ctx, constants, imageSnoopy } from './index.js';
import { Agent } from './Agents.js';
import { Bullet } from './Bullet.js';
import { Camera, DebugCamera } from './Scene.js';
import { GridBackground } from './GridBackground.js';
import { origin } from './VectorMath.js';
/**
 * Holds the main game loop for this dogfighting game. Holds the state and behavior necessary to
 * continuously run a game.
 */
var GameWorld = /** @class */ (function () {
    /**
     * Constructs a GameWorld from information that is available via a server update packet.
     * @param players the dogfighters to render
     * @param bullets the bullets to render
     * @param updateManager the manager for the server we're listening to that will provide us with
     * updates to our game objects
     */
    function GameWorld(updateManager) {
        var _this = this;
        /**
         * This will loop at the speed of constants.FPS, moving and animating all players and bullets on
         * the screen.
         */
        this.gameLoop = function () {
            // Only request continual updates once, on the first call of the gameLoop
            if (!_this.isRequestingUpdates) {
                _this.serverUpdateManager.beginRequestingUpdates();
                _this.isRequestingUpdates = true;
            }
            var now = Date.now();
            _this.millisPassedSinceLastFrame += now - _this.before;
            var millisPerFrame = 1000 / constants.FPS;
            if (_this.millisPassedSinceLastFrame >= millisPerFrame) {
                // Clear screen before next draw
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                // apply all updates, if any, to the players and bullets
                if (_this.serverUpdateManager.hasUpdate()) {
                    // Get most recent update
                    var serverUpdate = _this.serverUpdateManager.getUpdate();
                    var _loop_1 = function (playerID) {
                        // Check if player is new
                        var playerIsNew = !_this.players.hasOwnProperty(playerID);
                        if (playerIsNew) {
                            var agent_1 = new Agent(serverUpdate.players[playerID], function (pos, size) {
                                // TODO: figure out how to determine which player to draw (Snoopy vs Red Barron)
                                ctx.drawImage(imageSnoopy, pos.x, pos.y, size.x, size.y);
                            }, { x: 100, y: 150 } // Will need to change this for drawing Red Barron sprite
                            );
                            _this.players[playerID] = agent_1;
                            _this.camera.centerOn(agent_1.getPosition);
                            if (constants.DEBUG_MODE) {
                                ctx.font = "15px Arial";
                                _this.camera.addToDebugMenu(function () { return "Player ID: \"" + playerID + "\", Position: (" + Math.round(agent_1.getPosition().x) + ", " + Math.round(agent_1.getPosition().y) + ")"; });
                                _this.camera.addToDebugMenu(function () { return "Press \"s\" to simulate camera shake."; }); // TODO move this
                            }
                        }
                        _this.players[playerID].getServerUpdate(serverUpdate.players[playerID]);
                    };
                    // Update players
                    for (var playerID in serverUpdate.players) {
                        _loop_1(playerID);
                    }
                    // Update bullets
                    for (var bulletID in serverUpdate.bullets) {
                        var playerBullets = serverUpdate.bullets[bulletID];
                        // Check if bullet is new
                        var bulletIsNew = !_this.bullets.hasOwnProperty(bulletID);
                        if (bulletIsNew)
                            _this.bullets[bulletID] = [];
                        // Remove existing bullets from the scene
                        _this.bullets[bulletID] = [];
                        for (var _i = 0, playerBullets_1 = playerBullets; _i < playerBullets_1.length; _i++) {
                            var bullet = playerBullets_1[_i];
                            var bulletObj = new Bullet(bullet.position, bullet.velocity);
                            _this.bullets[bulletID].push(bulletObj);
                        }
                    }
                }
                // Update players
                for (var _a = 0, _b = Object.keys(_this.players); _a < _b.length; _a++) {
                    var playerID = _b[_a];
                    var secPerFrame = millisPerFrame / 1000;
                    var player = _this.players[playerID];
                    player.update(_this.millisPassedSinceLastFrame / 1000);
                }
                // Update bullets
                for (var _c = 0, _d = Object.keys(_this.bullets); _c < _d.length; _c++) {
                    var bulletID = _d[_c];
                    var secPerFrame = millisPerFrame / 1000;
                    for (var _e = 0, _f = _this.bullets[bulletID]; _e < _f.length; _e++) {
                        var bullet = _f[_e];
                        bullet.update(_this.millisPassedSinceLastFrame / 1000);
                    }
                }
                // Draw all game objects
                _this.camera.update(_this.millisPassedSinceLastFrame / 1000);
                _this.camera.renderAll();
                // Add debug info to the top left
                if (constants.DEBUG_MODE) {
                    _this.camera.displayDebugMenu();
                }
                _this.millisPassedSinceLastFrame = 0;
            }
            _this.before = now;
            requestAnimationFrame(_this.gameLoop);
        };
        // TODO
        this.getScene = function () {
            var scene = [];
            for (var _i = 0, _a = Object.values(_this.players); _i < _a.length; _i++) {
                var player = _a[_i];
                scene.push(player);
            }
            for (var _b = 0, _c = Object.values(_this.bullets); _b < _c.length; _b++) {
                var bulletGroup = _c[_b];
                for (var _d = 0, bulletGroup_1 = bulletGroup; _d < bulletGroup_1.length; _d++) {
                    var bullet = bulletGroup_1[_d];
                    scene.push(bullet);
                }
            }
            return scene;
        };
        this.players = {};
        this.bullets = {};
        this.serverUpdateManager = updateManager;
        this.isRequestingUpdates = false;
        this.before = Date.now();
        this.millisPassedSinceLastFrame = 0;
        this.scene = [];
        this.camera = constants.DEBUG_MODE
            ? new DebugCamera(this.getScene, new GridBackground())
            : new Camera(function () { return origin; }, this.getScene, new GridBackground());
    }
    return GameWorld;
}());
export { GameWorld };
//# sourceMappingURL=GameWorld.js.map