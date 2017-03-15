set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [437	1405	3203	6321	10871];
y2 = [198	983	2659	5250	9681];
y3 = [291	565	992	1581	2415];
y4 = [101	192	419	820	1283];

p1= plot(x, y1, '-k^');
hold on;
p2 = plot(x, y2, '-kv');
hold on;
p3 = plot(x, y3, '-ks');
hold on;
p4 = plot(x, y4, '-kd');

xlabel('percentage of vertices');
ylabel('time (ms)');

axis([0.5 5.5 0.0 12000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
leg=legend('Basic','Basic-','Advanced', 'Advanced-', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);