from django.db import models
from django.contrib.auth.models import User

class Player(models.Model):
    author = models.ForeignKey(User, null=False, blank=False)
    signatue = models.TextField() #mega unsicher