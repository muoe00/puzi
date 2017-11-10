package com.puzi.puzi.biz.channel;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */
@Data
public class ChannelEditorsPageVO implements Serializable {
	private String title;
	private String previewUrl;
	private String previewUrl2;
	private boolean showTitle;
	private String link;
	private String createdAt;
	private String createdBy;
}
