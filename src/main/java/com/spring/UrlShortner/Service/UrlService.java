package com.spring.UrlShortner.Service;

import org.springframework.stereotype.Service;

import com.spring.UrlShortner.Model.Url;
import com.spring.UrlShortner.Model.UrlDto;

@Service
public interface UrlService 
{
	public Url generateShortLink(UrlDto urldto);
	public Url persistShortLink(Url url);
	public Url getEncodeUrl(String url);
	public void deleteShortLink(Url url);
}
