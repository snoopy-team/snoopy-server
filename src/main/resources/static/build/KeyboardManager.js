// Examples
var doNothing = function () { };
export var doNothingKeyboardLayout = { onKeydown: doNothing, onKeyup: doNothing, onScroll: doNothing };
/**
 * Streamlines keyboard input so that only one set of keyboard controls is in effect at one time.
 */
var KeyboardManager = /** @class */ (function () {
    /**
     * Initialize listeners object with a single default that does nothing, listen to keybaord events
     * so we can stream events to the subscriber with this.currListenerID
     */
    function KeyboardManager() {
        var _this = this;
        // Create listeners and initialize with a keyboard control schema that does nothing.
        this.listeners = [];
        this.currListenerIdx = 0;
        this.listeners[0] = doNothingKeyboardLayout;
        // Listen to keyboard events so we can stream it to the current subscriber
        addEventListener('keydown', function (e) {
            // Cycle where to send input to
            if (e.key == 'c') {
                _this.currListenerIdx++;
            }
            _this.listeners[_this.currListenerIdx].onKeydown(e);
        });
        addEventListener('keyup', function (e) {
            _this.listeners[_this.currListenerIdx].onKeyup(e);
        });
        addEventListener('wheel', function (e) {
            _this.listeners[_this.currListenerIdx].onScroll(e);
        }, { passive: false });
    }
    /**
     * Adds this InputReceiver to the list of listeners of this KeyboardManager. When keys are
     * pressed, the key name (i.e. 'q') will be sent to the InputReceiver.
     * @param receiver where we'll route the events we get from the keyboard input
     */
    KeyboardManager.prototype.addListener = function (receiver) {
        this.listeners.push(receiver);
    };
    return KeyboardManager;
}());
export { KeyboardManager };
//# sourceMappingURL=KeyboardManager.js.map