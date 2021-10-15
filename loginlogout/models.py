from django.db import models


class User(models.Model):
    phone_number = models.CharField(max_length=255, null=False)
    name = models.EmailField(max_length=255, null=False)
    password = models.CharField(max_length=50)
    ifLogged = models.BooleanField(default=False)
    token = models.CharField(max_length=500, null=True, default="")

    def __str__(self):
        return "{} -{}".format(self.phone_number, self.name)

