package cn.snake.dbkit.manager;

import java.util.List;

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
     */
    public void insert(Object object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            dao.insert((ChatInfoModel) object);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            dao.insert((ContactModel) object);
        } else if (object instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            dao.insert((MemberInfo) object);
        }
    }

    /**
     * insert one data to db
     *
     * @param object
     */
    public void insertList(List<Object> object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            dao.insertInTx((ChatInfoModel) object);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            dao.insertInTx((ContactModel) object);
        } else if (object instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            dao.insertInTx((MemberInfo) object);
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
            ChatInfoModel chatInfoModel = dao.queryBuilder()
                    .where(ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ChatInfoModelDao.Properties.Jid.eq(((ChatInfoModel) object).getJid())
                            , ChatInfoModelDao.Properties.GroupId.eq(((ChatInfoModel) object).getGroupId()))
                    .unique();
            if (chatInfoModel != null)
                dao.deleteByKey(chatInfoModel.get_id());
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            ContactModel contactModel = dao.queryBuilder()
                    .where(ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ContactModelDao.Properties.Jid.eq(((ContactModel) object).getJid()),
                            ContactModelDao.Properties.GroupId.eq(((ContactModel) object).getGroupId()))
                    .unique();
            if (contactModel != null)
                dao.deleteByKey(contactModel.get_id());
        } else if (object instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            MemberInfo memberInfo = dao.queryBuilder().
                    where(MemberInfoDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            MemberInfoDao.Properties.Jid.eq(((MemberInfo) object).getJid()),
                            MemberInfoDao.Properties.GroupId.eq(((MemberInfo) object).getGroupId()))
                    .unique();
            if (memberInfo != null)
                dao.deleteByKey(memberInfo.get_id());
        }
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
                    .where(ChatInfoModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            ChatInfoModelDao.Properties.Jid.eq(((ChatInfoModel) object).getJid())
                            , ChatInfoModelDao.Properties.GroupId.eq(((ChatInfoModel) object).getGroupId()))
                    .unique();
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
            dao.update(model);
        } else if (object instanceof MemberInfo) {
            MemberInfoDao dao = DBHelper.getDaoSession().getMemberInfoDao();
            MemberInfo info = dao.queryBuilder().
                    where(MemberInfoDao.Properties.UserId.eq(ChatAccountHelper.getUserId()),
                            MemberInfoDao.Properties.Jid.eq(((MemberInfo) object).getJid()),
                            MemberInfoDao.Properties.GroupId.eq(((MemberInfo) object).getGroupId()))
                    .unique();
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
     * contains only chat and group chat
     *
     * @return
     */
    public List<ContactModel> getSessionList() {
        return DBHelper.getDaoSession().getContactModelDao().queryBuilder().where(
                ContactModelDao.Properties.UserId.eq(ChatAccountHelper.getUserId()))
                .orderAsc(ContactModelDao.Properties.LastTime)
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

}
