set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [1.900521577	29.70673392	227.3802585	699.0996448	1190.830284];
y2 = [1.840732327	22.50991918	90.85669938	211.8974526	352.8290257];
y3 = [0.364557873	30.68258376	14.86677778	31.54750328	74.07620972];


p1= plot(x, y1, '-kv');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('percentage of vertices');
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