var exec = require('cordova/exec');

var IntegrityPlugin = {
    getAPKHash: function(success, error) {
        exec(success, error, 'IntegrityPlugin', 'getAPKHash', []);
    }
};

module.exports = IntegrityPlugin;