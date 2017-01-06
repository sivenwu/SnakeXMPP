package cn.snake.dbkit.manager;

import com.snake.api.exceptions.SnakeRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snake.dbkit.DBHelper;
import cn.snake.dbkit.bean.ChatInfoModel;
import cn.snake.dbkit.bean.ContactModel;
import cn.snake.dbkit.bean.MemberInfo;
import cn.snake.dbkit.dao.ChatInfoModelDao;
import cn.snake.dbkit.dao.ContactModelDao;
import cn.snake.dbkit.dao.MemberInfoDao;
import cn.snake.dbkit.helper.ChatAccountHelper;

/**
 * Created by chenyk on 2016/12/15.
 * 数据库操作管理类
 */

public class DBOperationManager {
    private static DBOperationManager mManager;

    public static DBOperationManager get() {
        if (mManager == null) mManager = new DBOperationManager();
        return mManager;
    }

    /**
     * insert one data to db
     *
     * @param object
     * @return
     */
    public long insert(Object object) {
        if (object == null) throw new SnakeRuntimeException("The current object does not null");
        if (object instanceof ChatInfoModel) {
            ChatInfoModel chatInfoModel = ((ChatInfoModel) object);
            chatInfoModel.setUserId(ChatAccountHelper.getUserId());
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            return dao.insert(chatInfoModel);
        } else if (object instanceof ContactModel) {
            ContactModel contactModel = ((ContactModel) object);
            contactModel.setUserId(ChatAccountHelper.getUserId());
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            return dao.insert(contactModel);
        } else if (object instanceof MemberInfo) {
            MemberInfo memberInfo = ((MemberInfo) object);
            memberInfo.setUserId(ChatAccountHelper.getUserId());
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            return dao.insert(memberInfo);
        } else throw new SnakeRuntimeException("The current object does not exist");
    }

    /**
     * insert one data to db
     *
     * @param object
     */
    public void insertList(List<Object> object) {
        if (object.size() == 0) return;
        if (object.get(0) instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            List<ChatInfoModel> modelList = new ArrayList<>();
            for (Object model : object)
                modelList.add((ChatInfoModel) model);
            dao.insertInTx(modelList);
        } else if (object.get(0) instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            List<ContactModel> modelList = new ArrayList<>();
            for (Object model : object)
                modelList.add((ContactModel) model);
            dao.insertInTx(modelList);
        } else if (object.get(0) instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            List<MemberInfo> modelList = new ArrayList<>();
            for (Object model : object)
                modelList.add((MemberInfo) model);
            dao.insertInTx(modelList);
        }
    }

    /**
     * delete one data from db
     *
     * @param object jid and groupId must give
     */
    public void delete(Object object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            List<ChatInfoModel> modelList = dao.queryBuilder()
                    .where(ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ChatInfoModelDao.Properties.Jid.eq(((ChatInfoModel) object).getJid())
                            , ChatInfoModelDao.Properties.GroupId.eq(((ChatInfoModel) object).getGroupId()))
                    .list();
            if (modelList != null)
                dao.deleteInTx(modelList);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            List<ContactModel> modelList = dao.queryBuilder()
                    .where(ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ContactModelDao.Properties.Jid.eq(((ContactModel) object).getJid()),
                            ContactModelDao.Properties.GroupId.eq(((ContactModel) object).getGroupId()))
                    .list();
            if (modelList != null)
                dao.deleteInTx(modelList);
        } else if (object instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            List<MemberInfo> modelList = dao.queryBuilder().
                    where(MemberInfoDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            MemberInfoDao.Properties.Jid.eq(((MemberInfo) object).getJid()),
                            MemberInfoDao.Properties.GroupId.eq(((MemberInfo) object).getGroupId()))
                    .list();
            if (modelList != null)
                dao.deleteInTx(modelList);
        }
    }

    /**
     * 将联系人/群聊置顶
     *
     * @param jid
     */
    public void updateContactViewTop(String jid) {
        ContactModel model = new ContactModel();
        model.setJid(jid);
        model.setViewTop(1);
        update(model);
    }

    /**
     * update one data to db
     *
     * @param object
     */
    public void update(Object object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            ChatInfoModel model = dao.queryBuilder()
                    .where(ChatInfoModelDao.Properties._id.eq(((ChatInfoModel) object).get_id()),
                            ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ChatInfoModelDao.Properties.Jid.eq(((ChatInfoModel) object).getJid())
                            , ChatInfoModelDao.Properties.GroupId.eq(((ChatInfoModel) object).getGroupId()))
                    .unique();
            if (model == null)
                throw new SnakeRuntimeException("update fail , object does not exist");
            ChatInfoModel cacheModel = (ChatInfoModel) object;
            if (cacheModel.getGroupId() != 0)
                model.setGroupId(cacheModel.getGroupId());
            if (cacheModel.getJid() != null)
                model.setJid(cacheModel.getJid());
            if (cacheModel.getType() != null)
                model.setType(cacheModel.getType());
            if (cacheModel.getIsFrom() != null)
                model.setIsFrom(cacheModel.getIsFrom());
            if (cacheModel.getIsRead() != 0)
                model.setIsRead(cacheModel.getIsRead());
            if (cacheModel.getMessage() != null)
                model.setMessage(cacheModel.getMessage());
            if (cacheModel.getState() != null)
                model.setState(cacheModel.getState());
            if (cacheModel.getMessageStatus() != null)
                model.setMessageStatus(cacheModel.getMessageStatus());
            dao.update(model);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            ContactModel model = dao.queryBuilder()
                    .where(ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ContactModelDao.Properties.Jid.eq(((ContactModel) object).getJid()),
                            ContactModelDao.Properties.GroupId.eq(((ContactModel) object).getGroupId()))
                    .unique();
            if (model == null)
                throw new SnakeRuntimeException("update fail , object does not exist");
            ContactModel cacheModel = (ContactModel) object;
            if (cacheModel.getGroupId() != 0)
                model.setGroupId(cacheModel.getGroupId());
            if (cacheModel.getGroupName() != null)
                model.setGroupName(cacheModel.getGroupName());
            if (cacheModel.getGroupHeadPicUrl() != null)
                model.setGroupHeadPicUrl(cacheModel.getGroupHeadPicUrl());
            if (cacheModel.getUserName() != null)
                model.setUserName(cacheModel.getUserName());
            if (cacheModel.getLastMessage() != null)
                model.setLastMessage(cacheModel.getLastMessage());
            if (cacheModel.getLastTime() != 0)
                model.setLastTime(cacheModel.getLastTime());
            if (cacheModel.getHeadPicUrl() != null)
                model.setHeadPicUrl(cacheModel.getHeadPicUrl());
            if (cacheModel.getMobile() != null)
                model.setMobile(cacheModel.getMobile());
            if (cacheModel.getIsOnline() != null)
                model.setIsOnline(cacheModel.getIsOnline());
            dao.update(model);
        } else if (object instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            MemberInfo info = dao.queryBuilder().
                    where(MemberInfoDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            MemberInfoDao.Properties.Jid.eq(((MemberInfo) object).getJid()),
                            MemberInfoDao.Properties.GroupId.eq(((MemberInfo) object).getGroupId()))
                    .unique();
            if (info == null)
                throw new SnakeRuntimeException("update fail , object does not exist");
            MemberInfo cacheInfo = (MemberInfo) object;
            if (cacheInfo.getGroupId() != 0)
                info.setGroupId(cacheInfo.getGroupId());
            if (cacheInfo.getJoinTime() != null)
                info.setJoinTime(cacheInfo.getJoinTime());
            if (cacheInfo.getUserName() != null)
                info.setUserName(cacheInfo.getUserName());
            if (cacheInfo.getIsGroupMaster() != 0)
                info.setIsGroupMaster(cacheInfo.getIsGroupMaster());
            if (cacheInfo.getHeadPicUrl() != null)
                info.setHeadPicUrl(cacheInfo.getHeadPicUrl());
            if (cacheInfo.getOtherInfo() != null)
                info.setOtherInfo(cacheInfo.getOtherInfo());
            dao.update(info);
        }
    }

//    /**
//     * query datas from db
//     *
//     * @param obj
//     * @param <T>
//     * @return
//     */
//    public <T extends Object> List<T> queryList(Object obj) {
//        if (obj instanceof ChatInfoModel)
//            return (List<T>) DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder()
//                    .where(ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId())).list();
//        else if (obj instanceof ContactModel)
//            return (List<T>) DBHelper.getDaoSession().getContactModelDao().queryBuilder()
//                    .where(ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId())).list();
//        else if (obj instanceof MemberInfo)
//            return (List<T>) DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder()
//                    .where(MemberInfoDao.Properties.UserId.eq(ChatAccountHelper.getUserId())).list();
//        else throw new SnakeRuntimeException("The current object does not exist");
//    }


    /**
     * get does not read message counts
     * contains only chat and group chat
     *
     * @return
     */
    public int getNewMessageCounts() {
        return DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder().where(
                ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ChatInfoModelDao.Properties.IsRead.eq("1"))
                .build().list().size();
    }

    /**
     * get does not read message counts who jid
     * contains only chat and group chat
     *
     * @param jid
     * @return
     */
    public int getNewMessageCountsFromJid(String jid) {
        return DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder().where(
                ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ChatInfoModelDao.Properties.Jid.eq(jid),
                ChatInfoModelDao.Properties.IsRead.eq("1"))
                .build().list().size();
    }

    /**
     * get 指定用户（jid）的聊天 list / single chat
     *
     * @param jid
     * @return
     */
    public List<ChatInfoModel> getSingleChatList(String jid) {
        return DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder().where(
                ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ChatInfoModelDao.Properties.Jid.eq(jid),
                ChatInfoModelDao.Properties.GroupId.eq(-1))
                .build().list();
    }

    /**
     * get 群聊（groupId）的聊天 list / group chat
     *
     * @param groupId
     * @return
     */
    public List<ChatInfoModel> getGroupChatList(String groupId) {
        return DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder().where(
                ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ChatInfoModelDao.Properties.GroupId.eq(groupId))
                .build().list();
    }

    /**
     * get 会话 list 按时间排序
     * only chat 只有单聊，群聊先不考虑
     *
     * @return
     */
    public List<ContactModel> getSessionList() {
        // TODO: 2016/12/30 后续添加群聊
        return DBHelper.getDaoSession().getContactModelDao().queryBuilder().where(
                ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ContactModelDao.Properties.GroupId.eq(0))
                .orderAsc(ContactModelDao.Properties.LastTime)
                .build().list();
    }
    /**
     * get 联系人 list 按名字排序
     * only chat 只有单聊，群聊先不考虑
     *
     * @return
     */
    public List<ContactModel> getContactList() {
        // TODO: 2016/12/30 后续添加群聊
        return DBHelper.getDaoSession().getContactModelDao().queryBuilder().where(
                ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ContactModelDao.Properties.GroupId.eq(0))
                .orderAsc(ContactModelDao.Properties.UserName)
                .build().list();
    }

    /**
     * 是否存在某个用户
     *
     * @param jid
     * @return
     */
    public boolean isExistContact(String jid) {
        return DBHelper.getDaoSession().getContactModelDao().queryBuilder().where(
                ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                ContactModelDao.Properties.Jid.eq(jid),
                ContactModelDao.Properties.GroupId.eq(-1))
                .build().list().size() > 0;
    }

    /**
     * get db router message
     *
     * @return
     */
    public static Map<String, Object> getRouterTable() {
        Map<String, Object> map = new HashMap<>();
        map.put("ChatInfoModel", "cn.snake.dbkit.bean.ChatInfoModel");
        map.put("ContactModel", "cn.snake.dbkit.bean.ContactModel");
        map.put("GroupMemberInfo", "cn.snake.dbkit.bean.GroupMemberInfo");
        map.put("MemberInfo", "cn.snake.dbkit.bean.MemberInfo");
        return map;
    }

}
