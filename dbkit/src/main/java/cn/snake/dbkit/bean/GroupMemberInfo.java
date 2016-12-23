package cn.snake.dbkit.bean;

import java.util.List;

/**
 * Created by chenyk on 2016/12/23.
 * 群聊成员信息
 */

public class GroupMemberInfo {
    public List<MemberInfo> memberInfoList;

    public List<MemberInfo> getMemberInfoList() {
        return memberInfoList;
    }

    public void setMemberInfoList(List<MemberInfo> memberInfoList) {
        this.memberInfoList = memberInfoList;
    }

}