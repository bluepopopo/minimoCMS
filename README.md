minimoCMS - A developer-first CMS
==========

minimoCMS website: http://www.minimocms.com

[TOC]

## Introduction

minimoCMS allows you to define the structure of the *data* in your website
using a simple API. The API allows you to define MoPage's, and a MoPage can
contain combinations of 3 main types - MoDocument, MoList and MoItem. From
these types you can create detailed page structures. These pages do not
write or generate any html/css for your website, the look/feel/structure of
the website is left to the developer, minimoCMS only works with the *data*
in your site.

Once you've defined the data structure using the minimoCMS APIs you now
need to place the data in your site. minimoCMS does not do this directly
for you, you must first get the required page, and then insert the data
where it is required in your site. You may think that this is a down-side
to minimo as it requires more work for the developer, however it is
actually one of the unique benefits of minimo as it remains non-invasive,
your site isn't *controlled* by minimoCMS and in fact if ever you wanted to
move off minimo it would not be a complex task.

Although minimoCMS is flexible to use any web language/framework and any
storage engine or DB, for the purpose of simplicity this guide will cover
the defaults - Spark Java web framework http://www.sparkjava.com and
MongoDB http://www.mondodb.com .

Getting Started
------------------
First things first - create a web-app project and add the minimoCMS maven
dependency

```xml
<dependency>
   <groupId>com.minimocms</groupId>
   <artifactId>minimo</artifactId>
   <version>1.0</version>
</dependency>
```

Initialise
----------
Because we are running an All-in-One web-app (see Deployment Options) we
will initialize minimoCMS like so -

```java
 public static void main(String args[]){
        Minimo.init("minimoCMSSample", new
MongoDataStoreImpl("minimoCMSSample"));
        ...
}
```
This has done 2 things -
1. Initialised minimoCMS with the siteName specified in the first argument
2. Initialised the MongoDB data store, the store will use the same site
name "minimoCMSSample"
    Here is where you would specify your own implementation of
DataStoreInterface if you don't want to use MongoDB. Note in this example
you will need MongoDB running on your localhost for this data store to work.

Define Data
--------------
The Sample web-app creates data in a separate function, here is the full
main method

```java
public static void main(String args[]){
        Minimo.init("minimoCMSSample", new
MongoDataStoreImpl("minimoCMSSample"));
        if(Minimo.pages().size()==0)createData();
        Minimo.persistPages();
        createWeb(); // this will be covered later
}
```
Ignore the last line, that is where your web-app will be defined. The line
after init() checks to see if pages have already been defined, if not we
create the data. The second-last line persists or saves the pages which
were created.

Data Types
-------------
- **Page: MoPage.class**
   A Page is as the name suggests, intended to be used per page in your
website. It can contain any number of Documents, Lists and Items
- **Document: MoDoc.class**
   A Document is a structure intended for individual sections of a page.
Similar to a page it can contain a number of other Documents, Lists and
Items
- **List: MoList.class**
   A list is used for repititious content of consistent structure. E.g. a
blog, or a list of products. Because each list item must have the same
structure, you must define this structure when you create a list. The
structure is built using a MoDoc, which as previously mentioned can contain
Documents, Lists and Items. Therefore the only restriction on the list is
that each item must have the same structure (but not necessarily the same
data, this includes number of items in sub-lists).
- **Item: MoItem.class**
   An item is a piece of data which is actually rendered in a site.
MoItem.class is not used directly, but it is the supertype for a number of
other types of similar definition e.g. MoTextItem and MoImage. Items cannot
contain documents, lists or other items.

Example Data Definition
-----------------------------
I've included a simple example -

```java
page("Home").document("Main Content").list("Products").buildTemplate(new
MoDoc("Product"),doc->{
doc.addItem(new MoTextItem("Title"),
txt -> txt.setValue("My Product Title"));

doc.addItem(new MoTextAreaItem("Description"),
txt -> txt.setValue("A product by the people, for the people"));

doc.addItem(new MoImageItem("Image"),
f -> f.file(new ResourceUtil().getFileBytes("/assets/images/800x500.gif")));

doc.addItem(new MoTextItem("Buy Button"),
txt -> txt.setValue("Buy Now"));

doc.addItem(new MoTextItem("Info-Button"),
txt -> txt.setValue("More Info"));
});

page("Home").document("Body").list("Products").add(4);
```

This is quite a detailed definition, so let's break it down starting from
the top.
First all the methods we are using are statically defined in Minimo.class,
so we need to start with
```java
import static com.minimocms.Minimo;
```
Next we need to create a page. The following creates the page of name
"Home", or if the "Home" page were previously defined it would get this
page instance.
```java
page("Home")
```
With the page we create a document.
```java
page("Home").document("Main Content")
```
And in the document we create a list of products (we name this list
"Products").
```java
page("Home").document("Main Content").list("Products")
```
And as mentioned in the previous section, we need to define the structure
of the items in our list. We are going to define the items as MoDoc's, and
then we are going to 'build' (or define) the MoDoc.
```java
page("Home").document("Main Content").list("Products").buildTemplate(new
MoDoc("Product"), doc -> {
   //Our "Product" document is defined here
});
```



Deployment Options
------------------------
minimoCMS can be run with your website in two ways

1. All-in-one web-app
   Run the minimoCMS management console in the same web-app as your site.
The only restriction with this method is that you are required to use Spark
as your Java web framework

2. Separate CMS and web-app
   This method keeps the web-interface to minimoCMS separate to your
web-app. Your web-app will still need to use minimoCMS to define and get
data (or alternatively use the REST API). With the REST API you can use
minimoCMS with any language or web framework (e.g. PHP).
