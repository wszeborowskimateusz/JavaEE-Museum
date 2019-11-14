(function () {
    var webSocket = new WebSocket("ws://localhost:8080/museum-1.0-SNAPSHOT/web-sockets");

    webSocket.onmessage = function (_) {
        if (window.location.href === "http://localhost:8080/museum-1.0-SNAPSHOT/museum/museum_last_modified.xhtml") {
            window.location.href = "http://localhost:8080/museum-1.0-SNAPSHOT/museum/museum_last_modified.xhtml";
        }
    };
})();
