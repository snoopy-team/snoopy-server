var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
import { canvas } from "./index.js";
import { modVectors, multiplyVectors, origin, subractVectors } from "./VectorMath.js";
/**
 * Draws a line on the canvas from the points specified
 * @param from the starting point of the line
 * @param to the ending point of the line
 */
var drawLine = function (ctx, from, to) {
    ctx.beginPath();
    ctx.moveTo(from.x, from.y);
    ctx.lineTo(to.x, to.y);
    ctx.stroke();
};
/**
 * For drawing the borders of the world, which are the walls that Snoopy will not be able to go
 * past.
 */
var WorldBorderBackground = /** @class */ (function () {
    /**
     * Constructs a new WorldBorderbackground given a top left and bottom right of the world borders.
     * @param topLeft The top left of the boundary of the world in game units. Snoopy will start
     * out at the origin in absolute game units.
     * @param bottomRight The bottom right of the boundary of the world in game units.
     */
    function WorldBorderBackground(topLeft, bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
    /**
     * Given a point of origin and screen bounds (top left, bottom right), draws a grid background
     * where each grid square is the size given by `dimensions`
     * @param ctx our canvas drawing context
     * @param topLeft the absolute, world coordinate of the top left of our viewing area
     * @param bottomRight the absolute, world coordinate of the bottom right of our viewing area
     */
    WorldBorderBackground.prototype.draw = function (ctx, topLeft, bottomRight, scale, worldToScreenCoords) {
        // Define screen coords for lines
        var topLeftScreen = worldToScreenCoords(this.topLeft);
        var bottomRightScreen = worldToScreenCoords(this.bottomRight);
        var bottomLeftScreen = worldToScreenCoords({ x: this.topLeft.x, y: this.bottomRight.y });
        var topRightScreen = worldToScreenCoords({ x: this.bottomRight.x, y: this.topLeft.y });
        // Change the color of the border lines
        ctx.strokeStyle = 'red';
        var origLineWidth = ctx.lineWidth;
        ctx.lineWidth = 10;
        // Draw the borders: left, bottom, right, top
        drawLine(ctx, topLeftScreen, bottomLeftScreen);
        drawLine(ctx, bottomLeftScreen, bottomRightScreen);
        drawLine(ctx, bottomRightScreen, topRightScreen);
        drawLine(ctx, topRightScreen, topLeftScreen);
        // Reset strokeStyle to default
        ctx.strokeStyle = 'black';
        ctx.lineWidth = origLineWidth;
    };
    return WorldBorderBackground;
}());
export { WorldBorderBackground };
/**
 * TODO
 */
var WorldBorderWithGrid = /** @class */ (function (_super) {
    __extends(WorldBorderWithGrid, _super);
    /**
     * Constructs a new WorldBorderWithGrid with the given boundaries of the world
     * @param topLeft The top left of the boundary of the world in game units. Snoopy will start
     * out at the origin in absolute game units.
     * @param bottomRight The bottom right of the boundary of the world in game units.
     */
    function WorldBorderWithGrid(topLeft, bottomRight) {
        var _this = _super.call(this, topLeft, bottomRight) || this;
        _this.gridBackground = new GridBackground();
        return _this;
    }
    WorldBorderWithGrid.prototype.draw = function (ctx, topLeft, bottomRight, scale, worldToScreenCoords) {
        _super.prototype.draw.call(this, ctx, topLeft, bottomRight, scale, worldToScreenCoords);
        this.gridBackground.draw(ctx, topLeft, bottomRight, scale, worldToScreenCoords);
    };
    return WorldBorderWithGrid;
}(WorldBorderBackground));
export { WorldBorderWithGrid };
/**
 * For drawing grid lines as a background. This won't necessarily be used as our final background
 * for the project but it will serve the purpose to visually test our camera.
 */
var GridBackground = /** @class */ (function () {
    function GridBackground() {
        var _this = this;
        /**
         * Given a point of origin and screen bounds (top left, bottom right), draws a grid background
         * where each grid square is the size given by `dimensions`
         * @param ctx our canvas drawing context
         * @param topLeft the absolute, world coordinate of the top left of our viewing area
         * @param bottomRight the absolute, world coordinate of the bottom right of our viewing area
         */
        this.draw = function (ctx, topLeft, bottomRight, scale, worldToScreenCoords) {
            var dimensions = multiplyVectors(_this.gridSquareDimensions, { x: scale, y: scale });
            // This is the (x,y) of the intersection of the top, leftmost horizintal and vertical lines
            // Should be above and to the left of the `topLeft` viewing bound.
            var gridOriginOffset = modVectors(topLeft, dimensions);
            // This gets the screen coordinates of our grid origin. I.e. might be negative if the top left
            // of our screen is viewing a coordinate past the origin like (50, 50)
            var gridOrigin = subractVectors(origin, gridOriginOffset);
            // Draw vertical lines
            for (var x = gridOrigin.x; x <= canvas.width; x += dimensions.x) {
                var start = { x: x, y: 0 };
                var end = { x: x, y: canvas.height };
                drawLine(ctx, start, end);
            }
            // Draw horizontal lines
            for (var y = gridOrigin.y; y <= canvas.height; y += dimensions.y) {
                var start = { x: 0, y: y };
                var end = { x: canvas.width, y: y };
                drawLine(ctx, start, end);
            }
        };
        this.gridSquareDimensions = { x: 300, y: 300 };
    }
    // Draws the given text at the given coordinate with a circle behind it acting as a background.
    GridBackground.prototype.textWithCircleBackground = function (ctx, text, coord, radius) {
        // Store default so we can return to default after
        var defaultTextAlign = ctx.textAlign;
        ctx.arc(coord.x, coord.y, radius, 0, Math.PI * 2);
        ctx.fillStyle = 'black';
        ctx.fill();
        ctx.strokeStyle = 'white';
        ctx.textAlign = 'center';
        ctx.fillText(text, coord.x, coord.y);
        // Use stored default to return to normal
        ctx.textAlign = defaultTextAlign;
    };
    return GridBackground;
}());
export { GridBackground };
//# sourceMappingURL=GridBackground.js.map