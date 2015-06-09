# -*- coding: utf-8 -*-

# Scrapy settings for paylife project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#

BOT_NAME = 'paylife'

SPIDER_MODULES = ['paylife.spiders']
NEWSPIDER_MODULE = 'paylife.spiders'

ITEM_PIPELINES = {
 	'paylife.pipelines.DuplicatesPipeline': 100,
 	'paylife.pipelines.GeocodePipeline': 500,
 	'paylife.pipelines.CapitalizePipeline': 600
}

FEED_FORMAT = 'csv'
LOG_LEVEL = 'ERROR'

# Crawl responsibly by identifying yourself (and your website) on the user-agent
#USER_AGENT = 'paylife (+http://www.yourdomain.com)'
