from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
from argon2 import PasswordHasher
from argon2 import exceptions
import string
import datetime
import random


class SearchEndpoint(Resource):
    def get(self):
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])
        fields = []
        try:
            # Add Common Name
            common_name = {"label": "Common Name",
                           "type": "TEXT"}
            fields.append(common_name)

            # Add Scientific name
            scientific_name = {"label": "Scientific Name",
                               "type": "TEXT"}
            fields.append(scientific_name)

            # Add Class
            c = {"label": "Class",
                 "type": "DROP"}
            c['options'] = [cl.name for cl in session.query(Class).all()]
            fields.append(c)

            # Add Order
            o = {"label": "Order",
                 "type": "DROP"}
            o['options'] = [order.name for order in session.query(Order).all()]
            fields.append(o)

            # Add Familt
            f = {"label": "Family",
                 "type": "DROP"}
            f['options'] = [fa.name for fa in session.query(Family).all()]
            fields.append(f)

            # Add Genus
            g = {"label": "Genus",
                 "type": "DROP"}
            g['options'] = [ge.name for ge in session.query(Genus).all()]
            fields.append(g)

            # Add general type
            gt = {"label": "General Type",
                 "type": "DROP"}
            gt['options'] = [gty.name for gty in session.query(General_Type).all()]
            fields.append(gt)

            # Add mouth parts
            m = {"label": "Mouth Parts",
                 "type": "DROP"}
            m['options'] = [mp.name for mp in session.query(Mouth_Parts).all()]
            fields.append(m)

            # Add color
            colors = {"label": "Colors",
                      "type": "CHECK"}
            colors['options'] = [co.color for co in session.query(Color).all()]
            fields.append(colors)

            # Add wings
            wings = {"label": "has Wings",
                     "type": "CHECK"}
            wings['options'] = ["Yes"]
            fields.append(wings)

            # Add antenna
            antenna = {"label": "Has antenna",
                       "type": "CHECK"}
            antenna['options'] = ["Yes"]
            fields.append(antenna)

            # Add hing legs jump
            hl = {"label": "Has hind legs for jumping",
                  "type": "CHECK"}
            hl['options'] = ["Yes"]
            fields.append(hl)

            # Add hing hair
            hair = {"label": "Has hair",
                    "type": "CHECK"}
            hair['options'] = ["Yes"]
            fields.append(hair)

            # Add hing thin body
            thin = {"label": "Has thin body",
                    "type": "CHECK"}
            thin['options'] = ["Yes"]
            fields.append(thin)

            return jsonify({"success": 1,
                            "fields": fields})
        except Exception as e:
            return jsonify({"success": -1,
                            "error": str(e)})
