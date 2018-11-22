from flask import Flask
from flask_restful import reqparse, abort, Api, Resource
from endpoints.test import *
from endpoints.users import *
from endpoints.search import *
from endpoints.bug import *
from endpoints.sightings import *
from endpoints.pictures import *
from database.db import  getSession
from flask_cors import CORS

app = Flask(__name__)
app.config.from_pyfile('api.cfg')
api = Api(app)
cors = CORS(app, resources={r"/*": {"origins": "*"}})

api.add_resource(TestEndpoint, '/test')
api.add_resource(TestAuthEndpoint, '/testAuth')
api.add_resource(DbTestEndpoint, '/dbTest')
api.add_resource(UserRegisterEndpoint, '/register')
api.add_resource(LoginEndpoint, '/login')
api.add_resource(SearchEndpoint, '/search')
api.add_resource(BugRegisterEndpoint, '/registerBug')
api.add_resource(GetSightingsEndpoint, '/getSightings')
api.add_resource(AddSightingsEndpoint, '/addSighting')
api.add_resource(AddImageEndpoint, '/addImage')
api.add_resource(GetImagesEndpoint, '/getImages')
api.add_resource(FlagImagesEndpoint, '/flagImage')

if __name__ == '__main__':
    app.run(debug=False, port=8080)
