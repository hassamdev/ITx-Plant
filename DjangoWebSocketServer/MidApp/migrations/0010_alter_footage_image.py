# Generated by Django 4.2.6 on 2024-04-16 16:26

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('MidApp', '0009_alter_footage_image'),
    ]

    operations = [
        migrations.AlterField(
            model_name='footage',
            name='image',
            field=models.BinaryField(),
        ),
    ]
