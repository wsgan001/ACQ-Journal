set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [6.566845123	31.5497101	93.8820667	228.9473559	1190.830284];
y2 = [6.896003003	31.74334934	83.28949767	156.4477802	352.8290257];
y3 = [1.063284207	2.554516117	14.24343583	36.17485215	74.07620972];

p1= plot(x, y1, '-kv');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('percentage of keywords');
ylabel('time (ms)');

axis([0.5 5.5 0.0 1200]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
leg=legend('Inc-S','Inc-T','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);