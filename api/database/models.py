from sqlalchemy import Column
from sqlalchemy import ForeignKey
from sqlalchemy import Integer
from sqlalchemy import String
from sqlalchemy import Boolean
from sqlalchemy import Date
from sqlalchemy import Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship

Base = declarative_base()

class Submission(Base):
    __tablename__ = 'submissions'
    id = Column(Integer, primary_key=True)
    bug_id_old = Column(Integer, primary_key=False)
    bug_id_new = Column(Integer, primary_key=False)


class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    username = Column(String)
    password = Column(String)
    admin = Column(Boolean)
    sessions = relationship("Session", backref="user")

class Session(Base):
    __tablename__ = "session"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    # user field is created as a backref, accessing Session.user will give you access to the user tied to the session
    session_id = Column(String)
    api_key = Column(String)
    session_time = Column(Date)
    is_api = Column(Boolean)

class Bug(Base):
    __tablename__ = "bugs"

    def as_dict(self):
        d = {c: getattr(self, c) for c in ["common_name", "scientific_name", "id"]}
        if self.pictures:
            d["thumbnail"] = self.pictures[0].picture_link
        else:
            d["thumbnail"] = "https://i.imgur.com/DhEi3hk.png"
        return d

    id = Column(Integer, primary_key=True)
    common_name = Column(String)
    scientific_name = Column(String)
    class_id = Column(Integer, ForeignKey("class.id"))
    # _class field is created as a backref, accessing Bug.class will give you access to the class tied to the bug
    order_id = Column(Integer, ForeignKey("order.id"))
    # order field is created as a backref, accessing Bug.order will give you access to the order tied to the bug
    family_id = Column(Integer, ForeignKey("family.id"))
    # family field is created as a backref, accessing Bug.family will give you access to the family tied to the bug
    genus_id = Column(Integer, ForeignKey("genus.id"))
    # genus field is created as a backref, accessing Bug.genus will give you access to the genus tied to the bug
    color_id_1 = Column(Integer, ForeignKey("color.id"))
    color_id_2 = Column(Integer, ForeignKey("color.id"))
    color1 = relationship("Color", foreign_keys=[color_id_1])
    color2 = relationship("Color", foreign_keys=[color_id_2])
    # colors field is created as a backref, accessing Bug.color will give you access to the color tied to the bug
    general_type_id = Column(Integer, ForeignKey("general_type.id"))
    # general_type field is created as a backref, accessing Bug.general_type will give you access to the general_type tied to the bug
    mouth_parts_id = Column(Integer, ForeignKey("mouth_parts.id"))
    # mouth_parts field is created as a backref, accessing Bug.mouth_parts will give you access to the mouth_parts tied to the bug
    wings = Column(Boolean)
    antenna = Column(Boolean)
    hind_legs_jump = Column(Boolean)
    hairy_furry = Column(Boolean)
    thin_body = Column(Boolean)
    description = Column(String)
    additional_advice = Column(String)
    approved = Column(Boolean)
    pictures = relationship("Picture", backref="bug")
    sightings = relationship("Sighting", backref="bug")

class Class(Base):
    __tablename__ = "class"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    bugs = relationship("Bug", backref = "_class")

class Order(Base):
    __tablename__ = "order"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    bugs = relationship("Bug", backref = "order")

class Family(Base):
    __tablename__ = "family"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    bugs = relationship("Bug", backref = "family")

class Genus(Base):
    __tablename__ = "genus"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    bugs = relationship("Bug", backref = "genus")

class Color(Base):
    __tablename__ = "color"
    id = Column(Integer, primary_key=True)
    color = Column(String)

class General_Type(Base):
    __tablename__ = "general_type"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    bugs = relationship("Bug", backref = "general_type")

class Mouth_Parts(Base):
    __tablename__ = "mouth_parts"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    bugs = relationship("Bug", backref = "mouth_parts")

class Picture(Base):
    __tablename__ = "pictures"
    id = Column(Integer,  primary_key=True)
    bugid = Column(Integer, ForeignKey("bugs.id"))
    # bug field is created as a backref, accessing Picture.bug will give you access to the bug tied to the picture
    num_flags = Column(Integer)
    picture_link = Column(String)

class Sighting(Base):
    __tablename__ = "sightings"
    id = Column(Integer, primary_key=True)
    bugid = Column(Integer, ForeignKey("bugs.id"))
    # bug field is created as a backref, accessing Sighting.bug will give you access to the bug tied to the sighting
    latitude = Column(Float)
    longitude = Column(Float)
