# OsmDestinationViewer

**OsmDestinationViewer** is a Java library that renders **road sign-style visualizations** based on the `destination=*` tag from OpenStreetMap elements.

⚠️ **This project is under active development and may undergo major changes.**

## 📚 About

This is **not a standalone application**, but a **rendering engine** intended to be embedded in other tools or plugins.

It focuses on the OpenStreetMap `destination=*` tag, which is typically used on roads, ramps, and junctions to describe the destinations shown on traffic signs.

This library is particularly useful for:

- **Mappers** who want to **visually validate** `destination` tags  
- Improving **navigation-related tagging**  
- Enhancing **data quality** for routing and mapping applications  

## 🔧 Features

- Parses `destination=*` tags from OpenStreetMap elements
- Generates visualizations styled like actual road signs 
- Currently supports **SVG output only**

## 🧩 Requirements

- Java 8  
- Maven for building and dependency management  

## 🚀 Installation

Clone the repository:

```bash
git clone https://github.com/fansanelli/OsmDestinationViewer.git
cd OsmDestinationViewer
```
Build with Maven:

```bash
mvn clean package
```

## 📎 Notes

This project is not affiliated with or endorsed by the OpenStreetMap Foundation.  

## 📄 License

This project is licensed under the **MIT License**.
