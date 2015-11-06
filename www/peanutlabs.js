function PeanlutLabsPlugin() {}

PeanlutLabsPlugin.prototype.initialize = function (appId, appKey, userId, dob, gender, success, error) {
	cordova.exec(success, error, 'Peanutlabs', 'initialize', [appId, appKey, userId, dob, gender]);
};

PeanlutLabsPlugin.prototype.showOfferwall = function (success, error) {
	cordova.exec(success, error, 'Peanutlabs', 'showOfferwall', []);
};


module.exports = new PeanlutLabsPlugin();