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
                            "error": str(e)})

class getBugEndpoint(Resource):
    def post(self):
        
        required_fields = ["id"]

        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})

        # Create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # Check to see if bug entry exists via id, if so, go on, if not,
        # post error
        try:
            bug = session.query(Bug).filter_by(id=json_data["id"]).first()
            if not bug:
                return jsonify({"success": -1,
                                "error": "Bug does not exist."})

            common_name = bug.common_name
            scientific_name = bug.scientific_name
            _class = bug._class.name
            order = bug.order.name
            family = bug.family.name
            genus = bug.genus.name
            color_1 = bug.color1.color
            color_2 = bug.color2.color
            general_type = bug.general_type.name
            mouth_parts = bug.mouth_parts.name
            wings = bug.wings
            antenna = bug.antenna
            hind_legs_jump = bug.hind_legs_jump
            hairy_furry = bug.hairy_furry
            description = bug.description
            additional_advice = bug.additional_advice  
            pictures = [{"url": s.picture_link} for s in bug.pictures if s.picture_link]
            sightings = [{"latitude": s.latitude, "longitude": s.longitude} for s in bug.sightings]

            return jsonify({"success": 1,
                            "common_name": common_name,
                            "scientific_name": scientific_name,
                            "class": _class,
                            "order": order,
                            "family": family,
                            "genus": genus,
                            "color1": color_1,
                            "color2": color_2,
                            "general_type": general_type,
                            "mouth_parts": mouth_parts,
                            "wings": wings,
                            "antenna": antenna,
                            "hind_legs_jump": hind_legs_jump,
                            "hairy_furry": hairy_furry,
                            "description": description,
                            "additional_advice": additional_advice,
                            "pictures": pictures,
                            "sightings": sightings}) 
                                       
        except Exception as e:
            return jsonify({"success": -1,
                            "error": "Error getting bug: " + str(e)})
