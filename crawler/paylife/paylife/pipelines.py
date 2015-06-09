# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

import geocoder
from scrapy.exceptions import DropItem



class DuplicatesPipeline(object):

    def __init__(self):
        self.address_seen = set()

    def process_item(self, item, spider):
        if item['address'] in self.address_seen:
            raise DropItem("Duplicate item found: %s" % item)
        else:
            self.address_seen.add(item['address'])
            return item


class GeocodePipeline(object):

	def process_item(self, item, spider):
		address = item['address']+', '+item['pcode']+' '+item['city']
		g = self.get_location(address)

		while(g.lat == None or g.lng == None ):
			g = self.get_location(address)

		item['lat'] = g.lat
		item['lng'] = g.lng

		return item

	def get_location(self, address):
		key_bing = 'AlGO9QXKhTKI8tTIqpEcWNVenBn7x1yqeWYhs1lVgSwDSUVHM1y7QQ2yo97zculq'
		g = geocoder.bing(address ,key=key_bing)
		return g




class CapitalizePipeline(object):
    def process_item(self, item, spider):

    	item['address'] = item['address'].lower()
    	item['address'] = item['address'].title()

    	item['city'] = item['city'].lower()
    	item['city'] = item['city'].title()

    	item['name'] = item['name'].lower()
    	item['name'] = item['name'].title()

        return item