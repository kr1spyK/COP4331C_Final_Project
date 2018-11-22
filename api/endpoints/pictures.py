from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
from auth import AuthedResource
import string
import base64


class GetImagesEndpoint(Resource):
    def post(self):
        required_fields = ["id"]
        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # check to see if username is taken
        try:
            q = session.query(Picture).filter_by(bugid=json_data["id"]).all()
            pictures = [s.picture_link for s in q]
            return jsonify({"success": 1, "images": pictures})
        except Exception as e:
            return jsonify({"success": -1,
                            "error": "Error getting images: " + str(e)})

class AddImageEndpoint(AuthedResource):
    def post(self):
        required_fields = ["id", "image"]
        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        try:
            new = Picture(bugid=json_data["id"],
                            num_flags = 0,
                            picture_link = "")
            session.add(new)
            session.commit()

            #TODO: update extension
            image_data = base64.b64decode(json_data["image"]).decode()
            if "php" in image_data or "<?" in image_data:
                raise Exception("Invalid upload")
            with open("/var/www/html/images/{}.jpg".format(new.id), "wb") as f:
                f.write(image_data)
            new.picture_link = "https://poosgroup5-u.cf/images/{}.jpg".format(new.id)
            session.commit()
            return jsonify({"success": 1})
        except Exception as e:
            return jsonify({"success": -1,
                            "error": "Error adding image to database: " + str(e)})
