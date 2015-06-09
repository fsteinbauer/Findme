# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy

class Node(scrapy.Item):
    name = scrapy.Field()
    user = scrapy.Field()
    country = scrapy.Field()
    pcode = scrapy.Field()
    city = scrapy.Field()
    address = scrapy.Field()
    lat = scrapy.Field()
    lng = scrapy.Field()
    cid = scrapy.Field()

