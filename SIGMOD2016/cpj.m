set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

y=[0.004114744	0.014629136	0.061589789;
0.028039785	0.361264942	0.564777217;
0.009407264	0.020327825	0.137301251;
0.002180447	0.057518651	0.089036003];

b=bar(y);

ylabel('CPJ');

axis([0.5 4.5 0.0 1.0]);

set(gca, 'xtick', 1:4, 'XTickLabel', {'Flickr','DBLP','Tencent','DBpedia'});
leg=legend('Global','Local','ACQ', 1);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
applyhatch(gcf,'\x/') ;
