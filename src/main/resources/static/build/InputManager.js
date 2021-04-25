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
        this.currLayoutIdx = 0;
        // Cycle through layouts when 'c' is pressed
        addEventListener('keydown', function (e) {
            if (e.key == 'c') {
                _this.cycle();
            }
        });
    }
    IOManager.prototype.add = function (camera, keyboardLayout) {
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
    return IOManager;
}());
export { IOManager };
//# sourceMappingURL=InputManager.js.map