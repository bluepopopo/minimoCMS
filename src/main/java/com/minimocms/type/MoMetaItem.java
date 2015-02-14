package com.minimocms.type;


public abstract class MoMetaItem extends MoItem {

    protected MoMetaItem(){super();}
    public MoMetaItem(String name) {
        super(name);
    }


    @Override
    public String text(){
        return "";
    }


    @Override
    public abstract MoMetaItem copy();

    @Override
    public MoMetaItem copyWithId() {
        MoMetaItem t = copy();
        t.setId(id());
        return t;
    }

    @Override
    public boolean existsChildById(String id) {
        return false;
    }


    @Override
    public String render(String path) {
        return "";
    }



    @Override
    public abstract String type();

    @Override
    public void setValue(String value) {
        throw new RuntimeException("Should not be calling setValue on a meta item");
    }

    @Override
    public String toString(){
        return text();
    }
}
