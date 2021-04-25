/**
 * Controls the input from a keyboard or server and the output via the Camera system.
 */
var IOManager = /** @class */ (function () {
    /**
     * Initializes a default IOManager with no layouts.
     */
    function IOManager() {
        var _this = this;
        this.layouts = [];
        this.keysDown = [];
        this.currLayoutIdx = 0;
        // Cycle through layouts when 'c' is pressed
        addEventListener('keydown', function (e) {
            if (e.key == 'c') {
                _this.cycle();
            }
            if (!_this.keysDown.includes(e.key.toLowerCase())) {
                _this.keysDown.push(e.key.toLowerCase());
            }
            _this.layouts[_this.currLayoutIdx].keyboardLayout.onKeydown(e);
        });
        addEventListener('keyup', function (e) {
            // Remove keys from our list of keys held down
            _this.keysDown = _this.keysDown.filter(function (key) { return key != e.key.toLowerCase(); });
            // Call the function associated with pressing a key down
            _this.layouts[_this.currLayoutIdx].keyboardLayout.onKeyup(e);
        });
        addEventListener('wheel', function (e) {
            _this.layouts[_this.currLayoutIdx].keyboardLayout.onScroll(e);
        }, { passive: false });
    }
    /**
     * Renders the output from the current camera
     */
    IOManager.prototype.renderOutput = function () {
        this.layouts[this.currLayoutIdx].camera.renderAll();
    };
    /**
     * Adds a new pair of inputs and outputs. For example, you might want to take inputs from the
     * keyboard to control the player and follow the player with a camera. You may also want to take
     * inputs to manually pan around with arrow keys and simply follow this manually controlled panning
     * point.
     * @param camera the camera to render output from
     * @param keyboardLayout the functions that control where different kinds of input should be piped
     */
    IOManager.prototype.addIOPair = function (camera, keyboardLayout) {
        this.layouts.push({ camera: camera, keyboardLayout: keyboardLayout });
    };
    /**
     * Sets the current layout to the next layout in our list of layouts.
     */
    IOManager.prototype.cycle = function () {
        // Either increment our layout index by 1 or cycle back to 0 if we're at the end of the list
        if (this.currLayoutIdx >= (this.layouts.length - 1)) {
            this.currLayoutIdx = 0;
        }
        else {
            this.currLayoutIdx++;
        }
    };
    /**
     * Updates the current camera
     */
    IOManager.prototype.update = function (deltaTime) {
        this.layouts[this.currLayoutIdx].camera.update(deltaTime);
    };
    /**
     * Returns the keys that are currently being held down
     */
    IOManager.prototype.getKeysDown = function () {
        return this.keysDown;
    };
    return IOManager;
}());
export { IOManager };
//# sourceMappingURL=IOManager.js.map