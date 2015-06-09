
import scrapy
from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors import LinkExtractor
from paylife.items import Node
from scrapy.selector import Selector
from scrapy.http import HtmlResponse

class PaylifeSpider(CrawlSpider):

    def get_start_urls(start, end):
        urls = []
        for i in range(start, end+1):
            urls.append('http://www.paylife.at/web/content/de/Home/Service/Geldausgabeautomaten/National/standortliste.jsp?d-4033745-p='+str(i))

        return urls

    name = 'paylife'
    allowed_domains = ['paylife.at']
    start_urls = get_start_urls(1,888)
    #rules = [Rule(LinkExtractor(allow=['-p=\d+']), 'parse_atms')]
    #rules = [Rule(LinkExtractor(allow=()), 'parse_atms')]

    def parse(self, response):
        # atmTable = ATMTable()
        # atmTable['table'] = 'debug';
        nodes = []
        rows = response.xpath("//table[@class='kurstabelle']/tbody/tr").extract()
        for (i, item) in enumerate(rows):
            tds = Selector(text=item).xpath('//td/text()').extract()
           
            node = Node()
            node['name'] = tds[1]
            node['address'] = tds[4]
            node['pcode'] = tds[2]
            node['city'] = tds[3]
            node['country'] = 'Osterreich'
            node['lat'] = None
            node['lng'] = None
            node['user'] = 0
            node['cid'] = 1

            nodes.append(node)
                
        return nodes
        

