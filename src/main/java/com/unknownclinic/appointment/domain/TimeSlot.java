package com.unknownclinic.appointment.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSlot {
	
	private long id;
	
    @NotNull
    private long businessDayId;
    
    @NotNull
    private String slotType; // nullで終日営業、amまたはpmを格納
    
    @NotNull
    private boolean isAvailable;
    
    @NotNull
    private LocalDateTime createdAt;
    
 // --- 以下はDBには持たせない補助プロパティ ---

    /**
     * 時間帯ラベル番号（1〜14など、表示用/ロジック用。DBには対応カラムなし）
     */
    private Long labelNumber;

    /**
     * 画面表示用の時間帯ラベル（例: "09:00-09:30"）。DBには対応カラムなし
     */
    private String label;
}