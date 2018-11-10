from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
import json

class BugRegisterEndpoint(Resource):
    def post(self):
        
        # Define required fields
        required_fields = ["common_name", "scientific_name", "class_id",
        "order_id", "family_id", "genus_id", "color_id_1", "color_id_2", "general_type_id", 
        "mouth_parts_id", "wings", "antenna", "hind_legs_jump", "hairy_furry",
        "thin_body", "description", "additional_advice"]
        
        # Get JSON data from request
        # If any required field is missing, the missing field will be sent back in a JSON response
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})

        # Create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # Check to see if bug entry exists
        if session.query(Bug).filter_by(common_name=json_data["common_name"]).first():
            return jsonify({"success": -1,
                            "error": "Bug already registered"})

        # If there isn't a bug entry, make a new bug from the JSON data
        # and attempt to make a request to the database to add it
        try:
            new_bug = Bug(common_name=json_data["common_name"],
                          scientific_name=json_data["scientific_name"],
                          class_id=json_data["class_id"],
                          order_id=json_data["order_id"],
                          family_id=json_data["family_id"],
                          genus_id=json_data["genus_id"],
                          color_id_1=json_data["color_id_1"],
                          color_id_2=json_data["color_id_2"],
                          general_type_id=json_data["general_type_id"],
                          mouth_parts_id=json_data["mouth_parts_id"],
                          wings=json_data["wings"],
                          antenna=json_data["antenna"],
                          hind_legs_jump=json_data["hind_legs_jump"],
                          hairy_furry=json_data["hairy_furry"],
                          thin_body=json_data["thin_body"],
                          description=json_data["description"],
                          additional_advice=json_data["additional_advice"],
                          approved=False)
            session.add(new_bug)
            session.commit()

            # If successful, return 1, if not return -1 and error status
            return jsonify({"success": 1})
        except Exception as e:
            return jsonify({"success": -1, 
                            "error": "Error adding bug to db" + str(e)})