/**
 * 
 */
package issue1;

import java.util.List;

/**
 * @author liao
 *
 */
public class GroupMemberListResult {
    private int err_code;  
    private String err_msg;  
    private List<GroupMember> groupMemeberList;  
  
    public int getErr_code() {  
        return err_code;  
    }  
  
    public void setErr_code(int err_code) {  
        this.err_code = err_code;  
    }  
  
    public String getErr_msg() {  
        return err_msg;  
    }  
  
    public void setErr_msg(String err_msg) {  
        this.err_msg = err_msg;  
    }  
  
    public List<GroupMember> getGroupMemeberList() {  
        return groupMemeberList;  
    }  
  
    public void setGroupMemeberList(List<GroupMember> groupMemeberList) {  
        this.groupMemeberList = groupMemeberList;  
    }  
  
}
