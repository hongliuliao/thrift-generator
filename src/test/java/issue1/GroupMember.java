/**
 * 
 */
package issue1;

/**
 * @author liao
 *
 */
public class GroupMember {
    /** 
     * id 
     */  
    private long id;  
    /** 
     * 用户id 
     */  
    private long userId;  
    /** 
     * 备注名称 
     */  
    private String remarkName;  
    /** 
     * 群组id 
     */  
    private long groupId;  
    /** 
     * 用户类型 
     */  
    private int type;  
    /** 
     * 用户加入时间 
     */  
    private long joinTime;  
    /** 
     * 群是否锁定 
     */  
    private int isLock;  
    /** 
     * 群锁定时间 
     */  
    private long lockDate;  
    public long getId() {  
        return id;  
    }  
    public void setId(long id) {  
        this.id = id;  
    }  
    public long getUserId() {  
        return userId;  
    }  
    public void setUserId(long userId) {  
        this.userId = userId;  
    }  
    public String getRemarkName() {  
        return remarkName;  
    }  
    public void setRemarkName(String remarkName) {  
        this.remarkName = remarkName;  
    }  
    public long getGroupId() {  
        return groupId;  
    }  
    public void setGroupId(long groupId) {  
        this.groupId = groupId;  
    }  
    public int getType() {  
        return type;  
    }  
    public void setType(int type) {  
        this.type = type;  
    }  
    public long getJoinTime() {  
        return joinTime;  
    }  
    public void setJoinTime(long joinTime) {  
        this.joinTime = joinTime;  
    }  
    public int getIsLock() {  
        return isLock;  
    }  
    public void setIsLock(int isLock) {  
        this.isLock = isLock;  
    }  
    public long getLockDate() {  
        return lockDate;  
    }  
    public void setLockDate(long lockDate) {  
        this.lockDate = lockDate;  
    }  
}
