set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [2091	6455	13367	19939	26202];
y2 = [1714	6034	12317	19011	24429];
y3 = [520	946	1538	2234	2932];
y4 = [152	337	619	1004	1468];


p1= plot(x, y1, '-k^');
hold on;
p2 = plot(x, y2, '-kv');
hold on;
p3 = plot(x, y3, '-ks');
hold on;
p4 = plot(x, y4, '-kd');

xlabel('percentage of vertices');
ylabel('time (ms)');

axis([0.5 5.5 0.0 30000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
leg=legend('Basic','Basic-','Advanced', 'Advanced-', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);