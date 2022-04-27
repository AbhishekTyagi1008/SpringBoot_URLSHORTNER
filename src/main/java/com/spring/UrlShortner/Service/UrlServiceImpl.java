package com.spring.UrlShortner.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.UrlShortner.Model.Url;
import com.spring.UrlShortner.Model.UrlDto;
import com.spring.UrlShortner.Repository.UrlRepository;
import org.springframework.util.StringUtils;


import com.google.common.hash.Hashing;

@Component
public class UrlServiceImpl implements UrlService
{
	 private static final org.jboss.logging.Logger logger = LoggerFactory.logger(UrlServiceImpl.class);
	 
	 @Autowired
	 private UrlRepository urlRepository;

	@SuppressWarnings("deprecation")
	@Override
	public Url generateShortLink(UrlDto urldto) 
	{
		if(!StringUtils.isEmpty(urldto.getUrl()))
        {
            String encodedUrl = encodeUrl(urldto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urldto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urldto.getExpirationDate(),urlToPersist.getCreationDate()));
            Url urlToRet = persistShortLink(urlToPersist);

            if(urlToRet != null)
                return urlToRet;

            return null;
        }
        return null;
	}
	
	
	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate)
    {
        if(expirationDate==null)
        {
            return creationDate.plusSeconds(60);
        }
        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }

    private String encodeUrl(String url)
    {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodedUrl;
    }

	@Override
	public Url persistShortLink(Url url) 
	{
		 Url urlToRet = urlRepository.save(url);
	        return urlToRet;
	}

	@Override
	public Url getEncodeUrl(String string) {
		 Url urlToRet = urlRepository.findByShortLink(string);
	        return urlToRet;
	}

	@Override
	public void deleteShortLink(Url url) {
		  urlRepository.delete(url);
		
	}

}
