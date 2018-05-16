package hello.model;

import java.util.Date;

/**
 * Created by i-feng on 2017/8/31.
 */


public class Card {

    /*员工id*/
    private Integer id;

    /*姓名*/
    private String name;

    /*部门*/
    private String department;

    /*打卡次数*/
    private String count;

    /*打卡开始时间*/
    private String startTime;

    /*打卡结束时间*/
    private String endTime;

    /*请假类别（研发／非研发）*/
    private String leaveType;

    /*请假时长*/
    private String timeLength;

    /*描述*/
    private String description;

    /*请假（补休）开始时间*/
    private String timeOutStart;

    public void setTimeOutStart(String timeOutStart) {
        this.timeOutStart = timeOutStart;
    }

    public String getTimeOutStart() {
        return timeOutStart;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDepartment() {
        return department;
    }

    public Card() {
    }

    public Card(Integer id, String name, String department, String startTime, String endTime, String leaveType, String timeLength, String description, String timeOutStart)
    {
        this.id = id;
        this.name = name;
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
//        this.count = count;
        this.leaveType = leaveType;
        this.timeLength = timeLength;
        this.description = description;
        this.timeOutStart = timeOutStart;
    }

    public Card(Object object) {

    }

    @Override
    public String toString() {
        return "Card{" + "name='" + name + '\'' + ", id='" + id + '\'' +", department='" + department + '\'' + ", count='" + count + '\'' + ", startTime='" + startTime + '\'' + ", endTime='" + endTime + '\'' + ", timeLength='" + timeLength + '\'' + ", description='" + description + '\'' + ", timeOutStart='" + timeOutStart + '\'' + '}';
    }
}
