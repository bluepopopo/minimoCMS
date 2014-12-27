package com.minimocms.data;

import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;

import java.util.Collection;
import java.util.List;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public interface DataStoreInterface {

    public List<MoPage> pages();
    public List<MoUser> users();
    public MoPage page(String name);
    public MoUser user(String username);
    public void savePage(MoPage page);
    public void saveUser(MoUser user);
    public void savePages(Collection<MoPage> pages);
    public void saveUsers(Collection<MoUser> users);
    public List<MoId> fileIds();
    public byte[] file(MoId id);
    public MoId saveFile(byte[] bs);

}
