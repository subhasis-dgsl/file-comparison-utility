/**
 * 
 */
package main.utility;

/**
 * @author subhasis.swain
 *
 */
public enum Constants {
	
		 PATH("D:\\ws_bpm1\\file_compare_utility\\src\\com\\dgsl\\imp\\main\\resources\\config.properties"),
		 RIPE("whois.ripe.net"),
		 APNIC("whois.apnic.net"),
		 AFRINIC("whois.afrinic.net"),
		 LACNIC("whois.lacnic.net"),
		 JPNIC("whois.nic.ad.jp"),
		 KRNIC("whois.nic.or.kr"),
		 CNNIC("ipwhois.cnnic.cn"),
		 UNKNOWN("");

		    private String url;

		    Constants(String url) {
		        this.url = url;
		    }

		    public String url() {
		        return url;
		    }
		}

