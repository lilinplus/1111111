package com.baidu.call.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

//通话记录表
@Entity
@Table(name = "t_call_log")
@Data
public class CallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;//id

    @Column(name = "group_type")
    @Size(max = 10,message = "长度不得超过10")
    private String groupType;

    @Column(name = "caller_id")
    @Size(max = 80,message = "长度不得超过80")
    private String callerId;

    @Column(name = "call_src")
    @Size(max = 80,message = "长度不得超过80")
    private String callSrc;

    @Column(name = "call_src_num")
    @Size(max = 80,message = "长度不得超过80")
    private String callSrcNum;

    @Column(name = "call_dest")
    @Size(max = 80,message = "长度不得超过80")
    private String callDest;

    @Column(name = "call_dest_num")
    @Size(max = 80,message = "长度不得超过80")
    private String callDestNum;

    @Column(name = "dialplan_context")
    @Size(max = 80,message = "长度不得超过80")
    private String dialplanContext;

    @Column(name = "src_channel")
    @Size(max = 80,message = "长度不得超过80")
    private String srcChannel;

    @Column(name = "dest_channel")
    @Size(max = 80,message = "长度不得超过80")
    private String destChannel;

    @Column(name = "lastapp")
    @Size(max = 80,message = "长度不得超过80")
    private String lastapp;

    @Column(name = "lastdata")
    @Size(max = 80,message = "长度不得超过80")
    private String lastdata;

    @Column(name = "start_time")
    @Size(max = 80,message = "长度不得超过80")
    private String startTime;

    @Column(name = "end_time")
    @Size(max = 80,message = "长度不得超过80")
    private String endTime;

    @Column(name = "answer_time")
    @Size(max = 80,message = "长度不得超过80")
    private String answerTime;

    @Column(name = "duration")
    @Size(max = 11,message = "长度不得超过11")
    private Long duration;

    @Column(name = "bill_second")
    @Size(max = 11,message = "长度不得超过11")
    private Long billSecond;

    @Column(name = "call_status")
    @Size(max = 32,message = "长度不得超过32")
    private String callStatus;

    @Column(name = "ama_flags")
    @Size(max = 11,message = "长度不得超过11")
    private Long amaFlags;

    @Column(name = "account_pin_code")
    @Size(max = 150,message = "长度不得超过150")
    private String accountPinCode;

    @Column(name = "call_type")
    @Size(max = 32,message = "长度不得超过32")
    private String callType;

    @Column(name = "uniqueid")
    @Size(max = 80,message = "长度不得超过80")
    private String uniqueid;

    @Column(name = "trunk")
    @Size(max = 64,message = "长度不得超过64")
    private String trunk;

    @Column(name = "recording_filename")
    @Size(max = 256,message = "长度不得超过256")
    private String recordingFilename;

    @Column(name = "recording_timelen")
    @Size(max = 11,message = "长度不得超过11")
    private Long recordingTimelen;

    @Column(name = "recording_conf_filename")
    @Size(max = 256,message = "长度不得超过256")
    private String recordingConfFilename;

    @Column(name = "recording_conf_timelen")
    @Size(max = 11,message = "长度不得超过11")
    private Long recordingConfTimelen;

    @Column(name = "recording_onetouch_filename")
    @Size(max = 256,message = "长度不得超过256")
    private String recordingOnetouchFilename;

    @Column(name = "recording_onetouch_timelen")
    @Size(max = 11,message = "长度不得超过11")
    private Long recordingOnetouchTimelen;

    @Column(name = "conference_name")
    @Size(max = 16,message = "长度不得超过16")
    private String conferenceName;

    @Column(name = "ftp_upload")
    private Enum ftpUpload;

}
