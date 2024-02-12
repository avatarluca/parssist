let parsetree = "";

function Tree(data,
    {
      path, // as an alternative to id and parentId, returns an array identifier, imputing internal nodes
      id = Array.isArray(data) ? (d) => d.id : null, // if tabular data, given a d in data, returns a unique identifier (string)
      parentId = Array.isArray(data) ? (d) => d.parentId : null, // if tabular data, given a node d, returns its parent’s identifier
      children, // if hierarchical data, given a d in data, returns its children
      tree = d3.tree, // layout algorithm (typically d3.tree or d3.cluster)
      sort, // how to sort nodes prior to layout (e.g., (a, b) => d3.descending(a.height, b.height))
      label, // given a node d, returns the display name
      title, // given a node d, returns its hover text
      link, // given a node d, its link (if any)
      linkTarget = "_blank", // the target attribute for links (if any)
      width = 640, // outer width, in pixels
      height, // outer height, in pixels
      r = 5, // radius of nodes
      padding = 2, // horizontal padding for first and last column
      fill = "#999", // fill for nodes
      fillOpacity, // fill opacity for nodes
      stroke = "#222", // stroke for links
      strokeWidth = 1.5, // stroke width for links
      strokeOpacity = 0.4, // stroke opacity for links
      strokeLinejoin, // stroke line join for links
      strokeLinecap, // stroke line cap for links
      halo = "#fff", // color of label halo
      haloWidth = 3, // padding around the labels
      curve = d3.curveNatural, // curve for the link
      dyNode = 10 // vertical height of node
    } = {}
  ) {
    const root =
      path != null
        ? d3.stratify().path(path)(data)
        : id != null || parentId != null
        ? d3.stratify().id(id).parentId(parentId)(data)
        : d3.hierarchy(data, children);
  
    if (sort != null) root.sort(sort);
  
    const descendants = root.descendants();
    const L = label == null ? null : descendants.map((d) => label(d.data, d));
  
    const dx = dyNode; 
    const dy = (width / (root.height + padding)) * 0.9; 
    tree().nodeSize([dx, dy])(root);
  
    let x0 = Infinity;
    let x1 = -x0;
    root.each((d) => {
      if (d.x > x1) x1 = d.x;
      if (d.x < x0) x0 = d.x;
    });
  
    if (height === undefined) height = x1 - x0 + dx * 2;
  
    if (typeof curve !== "function") throw new Error(`Unsupported curve`);
  
    const svg = d3
      .create("svg")
      .attr("viewBox", [(-dy * padding) / 2, x0 - dx, width, height])
      .attr("width", width)
      .attr("height", height)
      .attr("style", "max-width: 100%; height: auto; height: intrinsic;")
      .attr("font-family", "sans-serif")
      .attr("font-size", 10);
  
    svg
      .append("g")
      .attr("fill", "none")
      .attr("stroke", stroke)
      .attr("stroke-opacity", strokeOpacity)
      .attr("stroke-linecap", strokeLinecap)
      .attr("stroke-linejoin", strokeLinejoin)
      .attr("stroke-width", strokeWidth)
      .selectAll("path")
      .data(root.links())
      .join("path")
      .attr(
        "d",
        d3
          .link(curve)
          .x((d) => d.y)
          .y((d) => d.x)
      );
  
    const node = svg
      .append("g")
      .selectAll("a")
      .data(root.descendants())
      .join("a")
      .attr("xlink:href", link == null ? null : (d) => link(d.data, d))
      .attr("target", link == null ? null : linkTarget)
      .attr("transform", (d) => `translate(${d.y},${d.x})`);
  
    node
      .append("circle")
      .attr("fill", (d) => (d.children ? stroke : fill))
      .attr("r", r);
  
    if (title != null) node.append("title").text((d) => title(d.data, d));
  
    if (L)
      node
        .append("text")
        .attr("dy", "0.32em")
        .attr("x", (d) => (d.children ? -6 : 6))
        .attr("text-anchor", (d) => (d.children ? "end" : "start"))
        .attr("paint-order", "stroke")
        .attr("stroke", halo)
        .attr("stroke-width", haloWidth)
        .text((d, i) => L[i]);
  
    return svg.node();
}