!function (e) {
    function t(n) {
        if (a[n]) return a[n].exports;
        var r = a[n] = {i: n, l: !1, exports: {}};
        return e[n].call(r.exports, r, r.exports, t), r.l = !0, r.exports
    }

    var a = {};
    t.m = e, t.c = a, t.d = function (e, a, n) {
        t.o(e, a) || Object.defineProperty(e, a, {configurable: !1, enumerable: !0, get: n})
    }, t.n = function (e) {
        var a = e && e.__esModule ? function () {
            return e.default
        } : function () {
            return e
        };
        return t.d(a, "a", a), a
    }, t.o = function (e, t) {
        return Object.prototype.hasOwnProperty.call(e, t)
    }, t.p = "", t(t.s = 4)
}([function (e, t, a) {
    "use strict";

    function n(e) {
        return e < 0 ? 0 : e >= 0 ? 1 : void 0
    }

    function r(e, t, a) {
        return t + a * e
    }

    function i(e, t) {
        return {x: t % e, y: Math.floor(t / e)}
    }

    function o(e, t) {
        for (var a = [], n = 0, r = e.length; n < r; n++) e[n].hasOwnProperty(t) && a.push(e[n][t]);
        return a
    }

    Object.defineProperty(t, "__esModule", {value: !0}), t.unitStep = n, t.get1DPosition = r, t.getCoordinate = i, t.valuesArray = o;
    var u = t.FACE_WIDTH = 180, c = t.FACE_HEIGHT = 180,
        s = (t.CAMERA_WIDTH = 640, t.CAMERA_HEIGHT = 480, t.CANVAS_WIDTH = 480), l = t.CANVAS_HEIGHT = 480,
        f = t.CAPTURE_HEIGHT = 180, h = t.CAPTURE_WIDTH = 180;
    t.FACE_FRAME = [(s - u) / 2, (l - c) / 2, u, c], t.POINT_8 = 8, t.RADIUS_1 = 1, t.RADIUS_2 = 2, t.RGBA_SHIFT = 4, t.NEIGHBOUR_SHIFT = 3, t.WEIGHT_BLOCK = [.15, .08, .15, .08, .15, .08, .08, .15, .08], t.UNIFORM_BINARY_PATTERN = ["non", 0, 1, 2, 3, 4, 6, 7, 8, 12, 14, 15, 16, 24, 28, 30, 31, 32, 48, 56, 60, 62, 63, 64, 96, 112, 120, 124, 126, 127, 128, 129, 131, 135, 143, 159, 191, 192, 193, 195, 199, 207, 223, 224, 225, 227, 231, 239, 240, 241, 243, 247, 248, 249, 251, 252, 253, 254, 255], t.BLOCK_9_BY_9 = [[0, 0, h / 3 + 1, f / 3 + 1], [h / 3 - 1, 0, h / 3 + 2, f / 3 + 1], [2 * h / 3 - 1, 0, h + 1, f / 3 + 1], [0, f / 3 - 1, h / 3 + 1, f / 3 + 2], [h / 3 - 1, f / 3 - 1, h / 3 + 2, f / 3 + 2], [2 * h / 3 - 1, f / 3 - 1, h + 1, f / 3 + 2], [0, 2 * f / 3 - 1, h / 3 + 1, f / 3 + 1], [h / 3 - 1, 2 * f / 3 - 1, h / 3 + 2, f / 3 + 1], [2 * h / 3 - 1, 2 * f / 3 - 1, h + 1, f / 3 + 1]], t.CHI_RECOGNITION_DOF = .02, t.CHI_RECOGNITION_BLOCKS_DOF = .05, t.CHI_PREDICTION_DOF = .1
}, function (e, t, a) {
    "use strict";

    function n(e) {
        return e && e.__esModule ? e : {default: e}
    }

    function r(e) {
        if (Array.isArray(e)) {
            for (var t = 0, a = Array(e.length); t < e.length; t++) a[t] = e[t];
            return a
        }
        return Array.from(e)
    }

    function i(e, t) {
        if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
    }

    Object.defineProperty(t, "__esModule", {value: !0});
    var o = function () {
        function e(e, t) {
            for (var a = 0; a < t.length; a++) {
                var n = t[a];
                n.enumerable = n.enumerable || !1, n.configurable = !0, "value" in n && (n.writable = !0), Object.defineProperty(e, n.key, n)
            }
        }

        return function (t, a, n) {
            return a && e(t.prototype, a), n && e(t, n), t
        }
    }(), u = a(2), c = n(u), s = a(8), l = a(0), f = function (e) {
        if (e && e.__esModule) return e;
        var t = {};
        if (null != e) for (var a in e) Object.prototype.hasOwnProperty.call(e, a) && (t[a] = e[a]);
        return t.default = e, t
    }(l), h = a(3), d = n(h), v = function () {
        function e() {
            i(this, e)
        }

        return o(e, null, [{
            key: "getImageData", value: function (e) {
                for (var t, a = arguments.length, n = Array(a > 1 ? a - 1 : 0), r = 1; r < a; r++) n[r - 1] = arguments[r];
                return 4 === n.length ? (t = e.getContext("2d")).getImageData.apply(t, n) : e.getContext("2d").getImageData(0, 0, e.width, e.height)
            }
        }, {
            key: "rgb2gray", value: function (e) {
                for (var t = 0, a = e.length; t < a; t += 4) e[t] = e[t + 1] = e[t + 2] = .3 * e[t] + .59 * e[t + 1] + .11 * e[t + 2];
                return e
            }
        }, {
            key: "rgbCanvas2grey", value: function (e) {
                var t = this.getImageData(e);
                return this.rgb2gray(t.data)
            }
        }, {
            key: "getPixelValue", value: function (e, t, a) {
                return e.getImageData(t, a, 1, 1).data[0]
            }
        }, {
            key: "extract8PointRadius1Feature", value: function (e) {
                for (var t = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : 1, a = e.getContext("2d"), n = this.getImageData(e), r = n.data, i = n.data.slice(), o = 1; o < e.height - 1; o++) for (var u = 1, c = 0; u < e.width - 1; u++, c += 4) {
                    var s = 0, l = [], h = f.get1DPosition(e.width, u, o) * f.RGBA_SHIFT,
                        d = this.getGrayScaleValue(i, h);
                    l[7] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u - t, o - t) * f.RGBA_SHIFT) - d, l[6] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u, o - t) * f.RGBA_SHIFT) - d, l[5] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u + t, o - t) * f.RGBA_SHIFT) - d, l[4] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u + t, o) * f.RGBA_SHIFT) - d, l[3] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u + t, o + t) * f.RGBA_SHIFT) - d, l[2] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u, o + t) * f.RGBA_SHIFT) - d, l[1] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u - t, o + t) * f.RGBA_SHIFT) - d, l[0] = this.getGrayScaleValue(i, f.get1DPosition(e.width, u - t, o) * f.RGBA_SHIFT) - d;
                    for (var v = 0, g = l.length; v < g; v++) s += f.unitStep(l[v]) * Math.pow(2, v);
                    r[h] = r[h + 1] = r[h + 2] = s
                }
                a.clearRect(0, 0, e.width, e.height), a.putImageData(n, 0, 0)
            }
        }, {
            key: "getGrayScaleValue", value: function (e, t) {
                return .3 * e[t] + .59 * e[t + 1] + .11 * e[t + 2]
            }
        }, {
            key: "evaluateRecognition", value: function (e) {
                this.extract8PointRadius1Feature(e);
                for (var t = {
                    value: 1,
                    name: "Unknown"
                }, a = [], n = 0, i = f.BLOCK_9_BY_9.length; n < i; n++) a = a.concat(f.valuesArray(d.default.uniformBinary(this.getImageData.apply(this, [e].concat(r(f.BLOCK_9_BY_9[n])))), "normalized"));
                if (this.compareWithData(a, Object.assign({}, c.default.get("data"), s.FACE_DATA), "blocks", t), t.value < f.CHI_RECOGNITION_BLOCKS_DOF) return t
            }
        }, {
            key: "compareWithData", value: function (e, t, a, n) {
                for (var r in t) if (t.hasOwnProperty(r)) {
                    var i = d.default.compareHistogram(e, t[r][a]);
                    i < n.value && (n.name = r, n.value = i)
                }
            }
        }]), e
    }();
    t.default = v
}, function (e, t, a) {
    "use strict";

    function n(e, t) {
        if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
    }

    Object.defineProperty(t, "__esModule", {value: !0});
    var r = function () {
        function e(e, t) {
            for (var a = 0; a < t.length; a++) {
                var n = t[a];
                n.enumerable = n.enumerable || !1, n.configurable = !0, "value" in n && (n.writable = !0), Object.defineProperty(e, n.key, n)
            }
        }

        return function (t, a, n) {
            return a && e(t.prototype, a), n && e(t, n), t
        }
    }(), i = function () {
        function e() {
            n(this, e)
        }

        return r(e, null, [{
            key: "has", value: function () {
                return !!sessionStorage.length
            }
        }, {
            key: "put", value: function (e, t) {
                sessionStorage[e] = JSON.stringify(t)
            }
        }, {
            key: "get", value: function (e, t) {
                return this.has() ? void 0 === t ? JSON.parse(sessionStorage[e]) : JSON.parse(sessionStorage[e])[t] : null
            }
        }]), e
    }();
    t.default = i
}, function (e, t, a) {
    "use strict";

    function n(e) {
        return e && e.__esModule ? e : {default: e}
    }

    function r(e) {
        if (Array.isArray(e)) {
            for (var t = 0, a = Array(e.length); t < e.length; t++) a[t] = e[t];
            return a
        }
        return Array.from(e)
    }

    function i(e, t) {
        if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
    }

    Object.defineProperty(t, "__esModule", {value: !0});
    var o = function () {
        function e(e, t) {
            for (var a = 0; a < t.length; a++) {
                var n = t[a];
                n.enumerable = n.enumerable || !1, n.configurable = !0, "value" in n && (n.writable = !0), Object.defineProperty(e, n.key, n)
            }
        }

        return function (t, a, n) {
            return a && e(t.prototype, a), n && e(t, n), t
        }
    }(), u = a(2), c = n(u), s = a(0), l = function (e) {
        if (e && e.__esModule) return e;
        var t = {};
        if (null != e) for (var a in e) Object.prototype.hasOwnProperty.call(e, a) && (t[a] = e[a]);
        return t.default = e, t
    }(s), f = a(1), h = n(f), d = function () {
        function e() {
            i(this, e)
        }

        return o(e, null, [{
            key: "init", value: function (e) {
                for (var t = [], a = 0, n = e.length; a < n; a++) t.push({bin: e[a], frequency: 0, normalized: 0});
                return t
            }
        }, {
            key: "uniformBinary", value: function (e) {
                for (var t = e.data, a = this.init(l.UNIFORM_BINARY_PATTERN), n = 0, r = t.length; n < r; n += 4) {
                    for (var i = !0, o = 1, u = l.UNIFORM_BINARY_PATTERN.length; o < u; o++) if (l.UNIFORM_BINARY_PATTERN[o] === t[n]) {
                        this.incrementHistogramFrequency(a, o, r), i = !1;
                        break
                    }
                    i && this.incrementHistogramFrequency(a, 0, r)
                }
                return a
            }
        }, {
            key: "incrementHistogramFrequency", value: function (e, t, a) {
                e[t].frequency++, e[t].normalized = e[t].frequency / (a / 4)
            }
        }, {
            key: "compareHistogram", value: function (e, t) {
                return this.weightedChiSquare(e, t)
            }
        }, {
            key: "chiSquare", value: function (e, t) {
                for (var a = e.length, n = 0, r = 0; r < a; r++) n += Math.pow(e[r] - t[r], 2) / t[r];
                return n
            }
        }, {
            key: "weightedChiSquare", value: function (e, t) {
                for (var a = e.length, n = 0, r = 0, i = 0; r < a; r++) i = Math.floor(r / l.UNIFORM_BINARY_PATTERN.length), n += l.WEIGHT_BLOCK[i] * Math.pow(e[r] - t[r], 2) / (e[r] + t[r]);
                return n
            }
        }, {
            key: "isNormalized", value: function (e, t) {
                for (var a = 0, n = 0, r = 1; r < e.length; r++) a += e[r].frequency, n += e[r].normalized;
                return a === t / 4 && 1 === Math.round(n)
            }
        }, {
            key: "generateHistogramValue", value: function (t, a) {
                var n = {}, i = [];
                c.default.has() && (n = c.default.get("data"));
                for (var o = 0, u = l.BLOCK_9_BY_9.length; o < u; o++) i = i.concat(l.valuesArray(e.uniformBinary(h.default.getImageData.apply(h.default, [t].concat(r(l.BLOCK_9_BY_9[o])))), "normalized"));
                n[a] = {
                    area: l.valuesArray(this.uniformBinary(h.default.getImageData(t)), "normalized"),
                    blocks: i
                }, c.default.put("data", n)
            }
        }]), e
    }();
    t.default = d
}, function (e, t, a) {
    a(5), e.exports = a(9)
}, function (e, t, a) {
    "use strict";
    var n = a(6), r = function (e) {
        return e && e.__esModule ? e : {default: e}
    }(n);
    r.default.createWithImage("camera", "images/lenna.png");
    var i = r.default.createById("camera"), o = document.querySelector("#webCam .fa");
    document.getElementById("webCam").addEventListener("click", function () {
        i.webcam.isActive ? (i.stopWebCam(), o.setAttribute("class", "fa fa-play fa-2x")) : (i.startWebCam(), o.setAttribute("class", "fa fa-pause fa-2x"))
    }), document.getElementById("capture").addEventListener("click", function () {
        if (i.webcam.isActive) {
            var e = document.querySelector(".name input");
            i.stopWebCam(), o.setAttribute("class", "fa fa-play fa-2x"), e.style.display = "block", e.onkeydown = function (t) {
                13 === t.keyCode && (i.capture(e.value), e.style.display = "none", e.value = "")
            }
        }
    })
}, function (e, t, a) {
    "use strict";

    function n(e) {
        return e && e.__esModule ? e : {default: e}
    }

    function r(e, t) {
        if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
    }

    Object.defineProperty(t, "__esModule", {value: !0});
    var i = function () {
        function e(e, t) {
            for (var a = 0; a < t.length; a++) {
                var n = t[a];
                n.enumerable = n.enumerable || !1, n.configurable = !0, "value" in n && (n.writable = !0), Object.defineProperty(e, n.key, n)
            }
        }

        return function (t, a, n) {
            return a && e(t.prototype, a), n && e(t, n), t
        }
    }(), o = a(7), u = n(o), c = a(0), s = function (e) {
        if (e && e.__esModule) return e;
        var t = {};
        if (null != e) for (var a in e) Object.prototype.hasOwnProperty.call(e, a) && (t[a] = e[a]);
        return t.default = e, t
    }(c), l = a(1), f = n(l), h = function () {
        function e(t) {
            r(this, e), this.canvas = t, this.width = this.canvas.width, this.height = this.canvas.height, this.capturedCanvas = document.getElementById("capturedImage"), this.capturedCanvas.width = s.CAPTURE_WIDTH, this.capturedCanvas.height = s.CAPTURE_HEIGHT, this.webcam = new u.default(this.canvas, this.capturedCanvas)
        }

        return i(e, [{
            key: "startWebCam", value: function () {
                this.webcam.start()
            }
        }, {
            key: "stopWebCam", value: function () {
                this.webcam.stop()
            }
        }, {
            key: "capture", value: function (e) {
                this.webcam.capture(e)
            }
        }, {
            key: "browseImage", value: function (e) {
                var t = this, a = new Image;
                a.src = e, a.onload = function () {
                    var e = s.CAPTURE_WIDTH / a.width, n = t.capturedCanvas.getContext("2d");
                    n.clearRect(0, 0, s.CAPTURE_WIDTH, s.CAPTURE_HEIGHT), n.drawImage(a, 0, (s.CAPTURE_HEIGHT - a.height * e) / 2, s.CAPTURE_WIDTH, a.height * e), f.default.extract8PointRadius1Feature(t.capturedCanvas)
                }
            }
        }, {
            key: "handleLocalFile", value: function (e) {
                e.type.match(/image.*/) && this.browseImage(window.URL.createObjectURL(e))
            }
        }], [{
            key: "createById", value: function (t) {
                var a = document.getElementById(t);
                return a.height = s.CANVAS_HEIGHT, a.width = s.CANVAS_WIDTH, new e(a)
            }
        }, {
            key: "createWithImage", value: function (t, a) {
                var n = e.createById(t);
                return n.browseImage(a), n
            }
        }]), e
    }();
    t.default = h
}, function (e, t, a) {
    "use strict";

    function n(e) {
        return e && e.__esModule ? e : {default: e}
    }

    function r(e) {
        if (Array.isArray(e)) {
            for (var t = 0, a = Array(e.length); t < e.length; t++) a[t] = e[t];
            return a
        }
        return Array.from(e)
    }

    function i(e, t) {
        if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
    }

    Object.defineProperty(t, "__esModule", {value: !0});
    var o = function () {
        function e(e, t) {
            for (var a = 0; a < t.length; a++) {
                var n = t[a];
                n.enumerable = n.enumerable || !1, n.configurable = !0, "value" in n && (n.writable = !0), Object.defineProperty(e, n.key, n)
            }
        }

        return function (t, a, n) {
            return a && e(t.prototype, a), n && e(t, n), t
        }
    }(), u = a(0), c = function (e) {
        if (e && e.__esModule) return e;
        var t = {};
        if (null != e) for (var a in e) Object.prototype.hasOwnProperty.call(e, a) && (t[a] = e[a]);
        return t.default = e, t
    }(u), s = a(1), l = n(s), f = a(3), h = n(f), d = function () {
        function e(t, a) {
            i(this, e), this.scaleV = 1, this.scaleH = -1, this.stream = "", this.isActive = !1, this.cameraTimeout = "", this.video = document.createElement("video"), this.canvas = t, this.capturedCanvas = a, this.context = this.canvas.getContext("2d"), this.capturedContext = this.capturedCanvas.getContext("2d")
        }

        return o(e, [{
            key: "start", value: function () {
                var e = this;
                navigator.getUserMedia({video: !0, audio: !1}, function (a) {
                    e.stream = a, e.video.src = window.URL.createObjectURL(a), t()
                }, function (e) {
                    console.log(e)
                });
                var t = function t() {
                    e.flipHorizontal(), e.drawRecognitionFrame(), e.capturedContext.drawImage(e.canvas, (c.CANVAS_WIDTH - c.FACE_WIDTH) / 2, (c.CANVAS_HEIGHT - c.FACE_HEIGHT) / 2, c.FACE_WIDTH, c.FACE_HEIGHT, 0, 0, e.capturedCanvas.width, e.capturedCanvas.height), e.cameraTimeout = setTimeout(t, 100, e.video, e.context), e.drawOutput(l.default.evaluateRecognition(e.capturedCanvas))
                };
                this.isActive = !0
            }
        }, {
            key: "stop", value: function () {
                clearTimeout(this.cameraTimeout), this.stream.getTracks()[0].stop(), this.isActive = !1
            }
        }, {
            key: "capture", value: function (e) {
                var t;
                this.capturedContext.clearRect(0, 0, this.capturedCanvas.width, this.capturedCanvas.height), (t = this.capturedContext).drawImage.apply(t, [this.canvas].concat(r(c.FACE_FRAME), [0, 0, this.capturedCanvas.width, this.capturedCanvas.height])), l.default.extract8PointRadius1Feature(this.capturedCanvas), h.default.generateHistogramValue(this.capturedCanvas, e)
            }
        }, {
            key: "flipHorizontal", value: function () {
                var e = (1 === this.scaleH ? 0 : -1 * c.CANVAS_WIDTH) + (c.CANVAS_WIDTH - c.CAMERA_WIDTH) / 2,
                    t = (1 === this.scaleV ? 0 : -1 * c.CANVAS_HEIGHT) + (c.CANVAS_HEIGHT - c.CAMERA_HEIGHT) / 2;
                this.context.save(), this.context.scale(this.scaleH, this.scaleV), this.context.drawImage(this.video, e, t), this.context.restore()
            }
        }, {
            key: "drawRecognitionFrame", value: function () {
                this.context.beginPath(), this.context.moveTo(c.FACE_FRAME[0], c.FACE_FRAME[1]), this.context.lineTo(c.FACE_FRAME[0] + c.FACE_FRAME[2], c.FACE_FRAME[1]), this.context.lineTo(c.FACE_FRAME[0] + c.FACE_FRAME[2], c.FACE_FRAME[1] + c.FACE_FRAME[3]), this.context.lineTo(c.FACE_FRAME[0], c.FACE_FRAME[1] + c.FACE_FRAME[3]), this.context.lineTo(c.FACE_FRAME[0], c.FACE_FRAME[1]), this.context.stroke(), this.context.closePath()
            }
        }, {
            key: "drawOutput", value: function (e) {
                void 0 !== e && (this.context.textAlign = "center", this.context.font = "bold 20pt Calibri", this.context.fillStyle = "#ffffff", this.context.fillText("You're", this.canvas.width / 2, this.canvas.height / 2 - 20), this.context.fillText(e.name, this.canvas.width / 2, this.canvas.height / 2 + 20))
            }
        }]), e
    }();
    t.default = d
}, function (e, t, a) {
    "use strict";
    Object.defineProperty(t, "__esModule", {value: !0});
    t.FACE_DATA = {}
}, function (e, t) {
}]);