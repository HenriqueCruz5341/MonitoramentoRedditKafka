package ufes.kafka.dto.profile;

import java.util.List;

import ufes.kafka.dto.blocked.ChildrenDto;

public class ProfileDto {
    private List<ChildrenDto> blockedUsers;
    private Integer numSubscribers;

    public List<ChildrenDto> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<ChildrenDto> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public Integer getNumSubscribers() {
        return numSubscribers;
    }

    public void setNumSubscribers(Integer numSubscribers) {
        this.numSubscribers = numSubscribers;
    }

    @Override
    public String toString() {
        return "ProfileDto [blockedUsers=" + blockedUsers + ", numSubscribers=" + numSubscribers + "]";
    }
}
