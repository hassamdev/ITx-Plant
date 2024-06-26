# Generated by Django 4.2.6 on 2024-03-29 09:36

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('MidApp', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='currentstatus',
            name='humidity',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='light_intensity',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='moisture',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='plant',
            field=models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, related_name='plantname', to='MidApp.plant'),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='soil_temp',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='temperature',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='water_level',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='currentstatus',
            name='water_ph',
            field=models.DecimalField(decimal_places=2, default=0, max_digits=3),
        ),
    ]
