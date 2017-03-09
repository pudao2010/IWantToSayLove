package com.bluewhaledt.saylove.ui.message.emoji;

public interface IEmoticonSelectedListener {
	void onEmojiSelected(String key);

	void onStickerSelected(String categoryName, String stickerName);
}
