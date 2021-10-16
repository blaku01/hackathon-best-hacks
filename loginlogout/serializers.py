from django.db.models import Q # for queries
from rest_framework import serializers
from rest_framework.validators import UniqueValidator
from django.core.validators import MinLengthValidator, MaxLengthValidator
from .models import User
from django.core.exceptions import ValidationError
from uuid import uuid4


class UserSerializer(serializers.ModelSerializer):
    name = serializers.CharField(
        required=True,
        validators=[]
        )
    phone_number = serializers.CharField(
        required=True,
        validators=[UniqueValidator(queryset=User.objects.all()), MinLengthValidator(9), MaxLengthValidator(9)]
        )
    password = serializers.CharField(max_length=8)

    class Meta:
        model = User
        fields = (
            'phone_number',
            'name',
            'password'
        )


class UserLoginSerializer(serializers.ModelSerializer):
    # to accept either phone_number or name
    phone_num = serializers.CharField()
    password = serializers.CharField()
    token = serializers.CharField(required=False, read_only=True)

    def validate(self, data):
        # user,name,password validator
        phone_num = data.get("user_id", None)
        password = data.get("password", None)
        if not phone_num and not password:
            raise ValidationError("Details not entered.")
        user = None
        # if the name has been passed
        if '@' in phone_num:
            user = User.objects.filter(
                Q(name=phone_num) &
                Q(password=password)
                ).distinct()
            if not user.exists():
                raise ValidationError("User credentials are not correct.")
            user = User.objects.get(name=phone_num)
        else:
            user = User.objects.filter(
                Q(phone_number=phone_num) &
                Q(password=password)
            ).distinct()
            if not user.exists():
                raise ValidationError("User credentials are not correct.")
            user = User.objects.get(phone_number=phone_num)
        if user.ifLogged:
            raise ValidationError("User already logged in.")
        user.ifLogged = True
        data['token'] = uuid4()
        user.token = data['token']
        user.save()
        return data

    class Meta:
        model = User
        fields = (
            'phone_num',
            'password',
            'token',
        )

        read_only_fields = (
            'token',
        )


class UserLogoutSerializer(serializers.ModelSerializer):
    token = serializers.CharField()
    status = serializers.CharField(required=False, read_only=True)

    def validate(self, data):
        token = data.get("token", None)
        print(token)
        user = None
        try:
            user = User.objects.get(token=token)
            if not user.ifLogged:
                raise ValidationError("User is not logged in.")
        except Exception as e:
            raise ValidationError(str(e))
        user.ifLogged = False
        user.token = ""
        user.save()
        data['status'] = "User is logged out."
        return data

    class Meta:
        model = User
        fields = (
            'token',
            'status',
        )
