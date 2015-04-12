/**
 * 
 */
package issue1;

/**
 * @author liao
 *
 */
public interface IGroupMemberQueryService {
//  /** 获取群成员基本信息  
//  * @param groupId  
//  * @param fromId      
//  *    
//  * @return  
//  *  如果没有该成员，返回空  
//  */  
// public GroupMemberResult getGroupMemberById(long groupId, long id, long fromId);  
     
   /**获取某个用户某个群的群成员清单（所有） 
    * @param fromId 
    * @param groupId 
    * @return 
    *  如果没有该群，返回空 
    */  
   public GroupMemberListResult getAllGroupMembers(long fromId, long groupId);  
}
