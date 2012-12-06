package com.sohu.thrift.generator.test.protobuf;

import java.util.List;
import java.util.Map;

import com.sohu.thrift.generator.test.protobuf.Cloudatlas.CodeMsg;
import com.sohu.thrift.generator.test.protobuf.Cloudatlas.Folder;
import com.sohu.thrift.generator.test.protobuf.Cloudatlas.InviteCode;
import com.sohu.thrift.generator.test.protobuf.Cloudatlas.User;

/**
 *
 * @author Caijianfeng
 *
 */
public interface IUserService {

	User registerByPassport(String passport);

	User registerWithInviteCode(String passport, String code);

	User registerWithOAuth(String oauthUid, String provider, String nick, String smallAvatar, String largeAvatar, String accessToken, long expires);

	User getUserByPassport(String passport);

	User getUserByUserId(long userId);

	boolean increaseQuota(long userId, long delta);

	boolean switchUserStatus(long userId, boolean blocked);

	/**
	 * check user quota and will be auto increase quota every 30days when out of quota
	 * @param userId
	 * @param fileSize
	 * @return
	 */
	public boolean checkQuota(long userId, int fileSize);

	public Map<Long, User> getUsersByIds(List<Long> userIds);

	public Map<String, User> getUsersByPassports(List<String> passports);

	/**
	 * update user describe
	 * @param userId
	 * @param bio user desc
	 * @return true:success,false:fail
	 */
	boolean updateBio(long userId, String bio);

	boolean updateUsage(long userId, long delta);
	
	/**
	 * 修改昵称
	 * @param userId
	 * @param newNick
	 * @return 0:成功,1:昵称被占用,2:昵称包含无效字符
	 */
	CodeMsg updateNick(long userId, String newNick);
	
	boolean updateSucAvatar(long userId);
	
	boolean updateAvatar(long userId, String smallAvatar, String largeAvatar);
	
	Folder findOrCreateDefaultFolder(long userId);
	
	boolean updateDefaultFolder(long userId, long folderId);

	// =================================InviteCode=================================

	String generateCode();

	List<InviteCode> listInviteCode(int start, int count);

}
