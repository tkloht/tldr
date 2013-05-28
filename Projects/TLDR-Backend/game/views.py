'''
Created on 21.05.2013

@author: dorian
'''
from django.shortcuts import render_to_response
from game.models import Article
from gcm.models import Device

def year_archive(request, year):
    a_list = Article.objects.filter(pub_date__year=year)
    return render_to_response('game/year_archive.html', {'year': year, 'article_list': a_list})

def list_devices(request):
    d_list = Device.objects.all()
    return render_to_response('game/listdevices.html', {'device_list': d_list})