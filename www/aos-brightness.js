var exec = require('cordova/exec');

exports.brightness = function(arg0, success, error) {
    exec(success, error, "aos-brightness", "brightness", [arg0]);
};
