# minimoCMS - A developer-first CMS

minimoCMS website: http://www.minimocms.com

[TOC]

## Introduction

minimoCMS allows you to define the structure of the *data* in your website using a simple API. The API allows you to define MoPage's, and a MoPage can contain combinations of 3 main types - MoDocument, MoList and MoItem. From these types you can create detailed page structures. These pages do not write or generate any html/css for your website, the look/feel/structure of the website is left to the developer, minimoCMS only works with the *data* in your site.

Once you've defined the data structure using the minimoCMS APIs you now need to place the data in your site. minimoCMS does not do this directly for you, you must first get the required page, and then insert the data
where it is required in your site. You may think that this is a down-side to minimo as it requires more work for the developer, however it is actually one of the unique benefits of minimo as it remains non-invasive,
your site isn't *controlled* by minimoCMS and in fact if ever you wanted to move off minimo it would not be a complex task.

Although minimoCMS is flexible to use any web language/framework and any storage engine or DB, for the purpose of simplicity this guide will cover the defaults - Spark Java web framework http://www.sparkjava.com and
MongoDB http://www.mondodb.com .

## Getting Started

### Prerequisites

First things first - create a web-app project and add the minimoCMS maven dependency

```xml
<dependency>
   <groupId>com.minimocms</groupId>
   <artifactId>minimo</artifactId>
   <version>1.0</version>
</dependency>
```
You will need Java 8 to run minimoCMS.
You will also need MongoDB running on localhost if you are using the default data store MongoDataStoreImpl (i.e. you are not planning to implement DataStoreInterface).

### Initialise
Because we are running an All-in-One web-app (see Deployment Options) we will initialize minimoCMS like so -

```java
 public static void main(String args[]){
        Minimo.init("minimoCMSSample", new MongoDataStoreImpl("minimoCMSSample"));
        ...
}
```
This has done 2 things -
1. Initialised minimoCMS with the siteName specified in the first argument 2. Initialised the MongoDB data store, the store will use the same site name "minimoCMSSample". Here is where you would specify your own implementation of DataStoreInterface if you don't want to use MongoDB. Note in this example you will need MongoDB running on your localhost for this data store to work.

### Define Data
The Sample web-app creates data in a separate function, here is the full main method

```java
public static void main(String args[]){
        Minimo.init("minimoCMSSample", new MongoDataStoreImpl("minimoCMSSample"));
        if(Minimo.pages().size()==0)createData();
        Minimo.persistPages();
        createWeb(); // this will be covered later
}
```
Ignore the last line, that is where your web-app will be defined. The line after init() checks to see if pages have already been defined, if not we create the data. The second-last line persists or saves the pages which were created.

### Data Types
- **Page**: MoPage.class
   A Page is as the name suggests, intended to be used per page in your website. It can contain any number of Documents, Lists and Items
- **Document**: MoDoc.class
   A Document is a structure intended for individual sections of a page. Similar to a page it can contain a number of other Documents, Lists and Items
- **List**: MoList.class
   A list is used for repititious content of consistent structure. E.g. a blog, or a list of products. Because each list item must have the same structure, you must define this structure when you create a list. The
structure is built using a MoDoc, which as previously mentioned can contain Documents, Lists and Items. Therefore the only restriction on the list is that each item must have the same structure (but not necessarily the same data, this includes number of items in sub-lists).
- **Item**: MoItem.class
   An item is a piece of data which is actually rendered in a site. MoItem.class is not used directly, but it is the supertype for a number of other types of similar definition e.g. MoTextItem and MoImage. Items cannot
contain documents, lists or other items.

### Items

## Examples

### Example Data Definition

I've included a simple example -

```java
page("Home").document("Main Content").list("Products").buildTemplate(new MoDoc("Product"),doc->{
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

This is quite a detailed definition, so let's break it down starting from the top. First all the methods we are using are statically defined in Minimo.class, so we need to start with
```java
import static com.minimocms.Minimo;
```
Next we need to create a page. The following creates the page of name "Home", or if the "Home" page were previously defined it would get this page instance.
```java
page("Home")
```
With the page we create a document.
```java
page("Home").document("Main Content")
```
And in the document we create a list of products (we name this list "Products").
```java
page("Home").document("Main Content").list("Products")
```
And as mentioned in the previous section, we need to define the structure of the items in our list. We are going to define the items as MoDoc's, and then we are going to 'build' (or define) the MoDoc.
```java
page("Home").document("Main Content").list("Products").buildTemplate(new MoDoc("Product"), doc -> {
   //Our "Product" document is defined here
});
```

### Example Data Placement in Site
Coming back to the main() method before, right after we defined and persisted the data definition we ignored the line with createWeb(). Simply it creates a web-route, inserts the data we created into our model and renders a velocity template with html/css (and our data mixed in). Here's the createWeb() method -
```java
private static void createWeb() {
   get("/",(req,resp)->{
       Map<String,Object> model = new HashMap<>();
       model.put("page",page("Home")); //the home page and all data in it
       return new ModelAndView(model,"/index.vm"); //the velocity template we're rendering
   }, new VelocityTemplateEngine());
}
```
So using Spark we've defined a default route "/", put our "Home" page definition in the model, and rendered our view with the "index.vm" velocity template. Of course you could potentially use any template engine here.

The next step is for the "index.vm" velocity template to render the html/css/js for the website with our data mixed in. I will skip the generic html/css code as we are most concerned about how the data is rendered. Let's have a look at an exerpt:

```html
<!-- Page Features -->
#foreach($doc in $page.document("Main Content").list("Products").items())
    <div class="col-md-3 col-sm-6 hero-feature">
        <div class="thumbnail">
            <img src='$doc.get("Image").text()' alt="">
            <div class="caption">
                <h3>$doc.get("Title").text()</h3>
                <p>$doc.get("Description").text()</p>
                <p>
                    <a href="#" class="btn btn-primary">$doc.item("Buy Button").text()</a>
                    <a href="#" class="btn btn-default">$doc.item("Info Button").text()</a>
                </p>
            </div>
        </div>
    </div>
#end
```
$page refers directly to our "Home" page we inserted into the model. From there you can see we are accessing the data in almost the exact same way as we defined it. We are iterating over the MoDoc's in our Products list, and rendering an image, title, description and 2 button. One thing we would include in a real website is a link for the Buy and Info buttons, something like this:
```html
<a href='$doc.item("Buy URL").text()' class="btn btn-primary">$doc.item("Buy Button").text()</a>
<a href='$doc.item("Info URL").text()' class="btn btn-default">$doc.item("Info Button").text()</a>
```


## Deployment Options
minimoCMS can be run with your website in two ways

1. All-in-one web-app
   Run the minimoCMS management console in the same web-app as your site. The only restriction with this method is that you are required to use Spark as your Java web framework

2. Separate CMS and web-app
   This method keeps the web-interface to minimoCMS separate to your web-app. Your web-app will still need to use minimoCMS to define and get data (or alternatively use the REST API). With the REST API you can use minimoCMS with any language or web framework (e.g. PHP).
