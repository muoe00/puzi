package com.puzi.puzi.biz.advertisement;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by muoe0 on 2017-04-28.
 */

@Data
public class ReceivedAdvertiseVO implements Serializable {

	private int receivedAdvertiseId;
	private int cmpnId;
	private int channelId;
	private String sendComment;
	private String link;
	private String linkPreviewUrl;
	private int viewSeconds;
	private String quiz;
	private String answerOne;
	private String answerTwo;
	private String receivedAt;
	private boolean today;
	private boolean saved;
	private CompanyVO companyInfoDTO;

	public boolean getToday() {
		return this.today;
	}

	public boolean getSaved() {
		return this.saved;
	}
/*
	protected ReceivedAdvertiseVO(Parcel in) {
		receivedAdvertiseId = in.readInt();
		cmpnId = in.readInt();
		channelId = in.readInt();
		sendComment = in.readString();
		link = in.readString();
		linkPreviewUrl = in.readString();
		viewSeconds = in.readInt();
		quiz = in.readString();
		answerOne = in.readString();
		answerTwo = in.readString();
		receivedAt = in.readString();
		today = in.readByte() != 0;
		saved = in.readByte() != 0;
	}

	public static final Creator<ReceivedAdvertiseVO> CREATOR = new Creator<ReceivedAdvertiseVO>() {
		@Override
		public ReceivedAdvertiseVO createFromParcel(Parcel in) {
			return new ReceivedAdvertiseVO(in);
		}

		@Override
		public ReceivedAdvertiseVO[] newArray(int size) {
			return new ReceivedAdvertiseVO[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(channelId);
		dest.writeString(link);
		dest.writeString(quiz);
		dest.writeString(answerOne);
		dest.writeString(answerTwo);
	}*/
}
