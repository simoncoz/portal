/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */

'use strict';

angular.module('ecompApp').directive('imageUpload', function factory($q) {
    var imageMimeRgx = /^image\/[a-zA-Z0-9]*$/;

    var URL = window.URL || window.webkitURL;

    var getResizeArea = function () {
        var resizeAreaId = 'fileupload-resize-area';

        var resizeArea = document.getElementById(resizeAreaId);

        if (!resizeArea) {
            resizeArea = document.createElement('canvas');
            resizeArea.id = resizeAreaId;
            resizeArea.style.visibility = 'hidden';
            document.body.appendChild(resizeArea);
        }

        return resizeArea;
    };

    var resizeImage = function (origImage, options) {
        var maxHeight = options.resizeMaxHeight || 300;
        var maxWidth = options.resizeMaxWidth || 250;
        var quality = options.resizeQuality || 0.7;
        var type = options.resizeType || 'image/jpg';

        var canvas = getResizeArea();

        var height = origImage.height;
        var width = origImage.width;

        //image redraw starting points
        var x0, y0;

        // calculate the width and height, constraining the proportions
        if (width > height) {
            if (width > maxWidth) {
                height = Math.round(height *= maxWidth / width);
                width = maxWidth;

                x0 = 0;
                y0 = Math.round((maxHeight - height)/2);
            }else{
                maxHeight = height;
                maxWidth = width;
                x0 = 0;
                y0 = 0;
            }
        } else {
            if (height > maxHeight) {
                width = Math.round(width *= maxHeight / height);
                height = maxHeight;

                x0 = Math.round((maxWidth - width)/2);
                y0 = 0;
            }else{
                maxHeight = height;
                maxWidth = width;
                x0 = 0;
                y0 = 0;
            }
        }

        canvas.width = maxWidth;
        canvas.height = maxHeight;

        //draw image on canvas
        var ctx = canvas.getContext("2d");

        //set background color
        if(options.backgroundColor){
            ctx.fillStyle = options.backgroundColor;
            ctx.fillRect(0,0,maxWidth,maxHeight);
        }


        ctx.drawImage(origImage, x0, y0, width, height);

        // get the data from canvas as 70% jpg (or specified type).
        return canvas.toDataURL(type, quality);
    };

    var createImage = function(url, callback) {
        var image = new Image();
        image.onload = function() {
            callback(image);
        };
        image.src = url;
    };

    var fileToDataURL = function (file) {
        var deferred = $q.defer();
        var reader = new FileReader();
        reader.onload = function (e) {
            deferred.resolve(e.target.result);
        };
        reader.readAsDataURL(file);
        return deferred.promise;
    };

    return {
        restrict: 'A',
        require: '^form',
        scope: {
            image: '=imageUpload',
            resizeMaxHeight: '@?imageUploadResizeMaxHeight',
            resizeMaxWidth: '@?imageUploadResizeMaxWidth',
            resizeQuality: '@?imageUploadResizeQuality',
            resizeType: '@?imageUploadResizeType',
            imageApi: '=?imageUploadApi',
            backgroundColor: '@?imageUploadBackgroundColor'
        },
        compile: function compile(tElement, tAttrs, transclude) {
            return function postLink(scope, iElement, iAttrs, formCtrl) {
                var doResizing = function(imageResult, callback) {
                    createImage(imageResult.url, function(image) {
                        var dataURL = resizeImage(image, scope);
                        imageResult.resized = {
                            dataURL: dataURL,
                            type: dataURL.match(/:(.+\/.+);/)[1]
                        };
                        callback(imageResult);
                    });
                };

                var applyScope = function(imageResult) {
                    scope.$apply(function() {
                        //console.log(imageResult);
                        if(iAttrs.multiple)
                            scope.image.push(imageResult);
                        else
                            scope.image = imageResult;
                    });
                };

                iElement.bind('change', function (evt) {
                    //when multiple always return an array of images
                    if(iAttrs.multiple)
                        scope.image = [];

                    var files = evt.target.files;
                    for(var i = 0; i < files.length; i++) {
                        setInputValidity(files[i]);

                        //create a result object for each file in files
                        var imageResult = {
                            file: files[i],
                            url: URL.createObjectURL(files[i])
                        };

                        fileToDataURL(files[i]).then(function (dataURL) {
                            imageResult.dataURL = dataURL;
                        });

                        if(scope.resizeMaxHeight || scope.resizeMaxWidth) { //resize image
                            doResizing(imageResult, function(imageResult) {
                                applyScope(imageResult);
                            });
                        }
                        else { //no resizing
                            applyScope(imageResult);
                        }
                    }
                });

                //API for otter actions
                scope.imageApi = scope.imageApi || {};
                scope.imageApi.clearFile = () => {
                    iElement[0].value = "";
                    setInputValidity();
                };


                let setInputValidity = file => {
                    //if form validation supported

                    if(formCtrl && iAttrs.name && formCtrl[iAttrs.name]){
                        formCtrl[iAttrs.name].$setDirty();
                        if(file && file.type && !imageMimeRgx.test(file.type)){
                            //set form invalid
                            formCtrl[iAttrs.name].$setValidity('mimeType', false);
                            applyScope();
                            return;
                        }
                        if(file && file.size && file.size > 1000000){
                            //set form invalid
                            formCtrl[iAttrs.name].$setValidity('imageSize', false);
                            applyScope();
                            return;
                        }
                        //set valid
                        formCtrl[iAttrs.name].$setValidity('mimeType', true);
                        formCtrl[iAttrs.name].$setValidity('imageSize', true);
                    }

                }
            }
        }
    }
});
